package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class SilkMothHiveBlock extends BlockBase {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final IntProperty FULLNESS = BlockProperties.FULLNESS;
	
	public SilkMothHiveBlock() {
		super(FabricBlockSettings.of(Material.WOOD).hardness(0.5F).resistance(0.1F).sounds(BlockSoundGroup.WOOL).nonOpaque().ticksRandomly().breakByHand(true));
		this.setDefaultState(getDefaultState().with(FULLNESS, 0));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING, FULLNESS);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getPlayerFacing().getOpposite();
		return this.getDefaultState().with(FACING, dir);
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Direction dir = state.get(FACING);
		BlockPos spawn = pos.offset(dir);
		if (!world.getBlockState(spawn).isAir()) {
			return;
		}
		int count = world.getEntitiesByType(EndEntities.SILK_MOTH, new Box(pos).expand(16), (entity) -> { return true; }).size();
		if (count > 6) {
			return;
		}
		SilkMothEntity moth = new SilkMothEntity(EndEntities.SILK_MOTH, world);
		moth.refreshPositionAndAngles(spawn.getX() + 0.5, spawn.getY() + 0.5, spawn.getZ() + 0.5, dir.asRotation(), 0);
		moth.setVelocity(new Vec3d(dir.getOffsetX() * 0.4, 0, dir.getOffsetZ() * 0.4));
		moth.setHive(world, pos);
		world.spawnEntity(moth);
		world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1, 1);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND) {
			ItemStack stack = player.getMainHandStack();
			if (stack.getItem().isIn(FabricToolTags.SHEARS) && state.get(FULLNESS) == 3) {
				BlocksHelper.setWithUpdate(world, pos, state.with(FULLNESS, 0));
				Direction dir = state.get(FACING);
				double px = pos.getX() + dir.getOffsetX() + 0.5;
				double py = pos.getY() + dir.getOffsetY() + 0.5;
				double pz = pos.getZ() + dir.getOffsetZ() + 0.5;
				ItemStack drop = new ItemStack(EndItems.SILK_FIBER, MHelper.randRange(8, 16, world.getRandom()));
				ItemEntity entity = new ItemEntity(world, px, py, pz, drop);
				world.spawnEntity(entity);
				if (world.getRandom().nextInt(4) == 0) {
					drop = new ItemStack(EndItems.SILK_MOTH_MATRIX);
					entity = new ItemEntity(world, px, py, pz, drop);
					world.spawnEntity(entity);
				}
				if (!player.isCreative()) {
					stack.setDamage(stack.getDamage() + 1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}
}
