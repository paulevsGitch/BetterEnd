package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.core.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class SilkMothHiveBlock extends BlockBase {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final IntegerProperty FULLNESS = BlockProperties.FULLNESS;

	public SilkMothHiveBlock() {
		super(FabricBlockSettings.of(Material.WOOD).hardness(0.5F).resistance(0.1F).sounds(SoundType.WOOL).nonOpaque()
				.ticksRandomly().breakByHand(true));
		this.setDefaultState(getDefaultState().with(FULLNESS, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING, FULLNESS);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getPlayerFacing().getOpposite();
		return this.defaultBlockState().with(FACING, dir);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		Direction dir = state.getValue(FACING);
		BlockPos spawn = pos.relative(dir);
		if (!world.getBlockState(spawn).isAir()) {
			return;
		}
		int count = world.getEntitiesByType(EndEntities.SILK_MOTH, new Box(pos).expand(16), (entity) -> {
			return true;
		}).size();
		if (count > 6) {
			return;
		}
		SilkMothEntity moth = new SilkMothEntity(EndEntities.SILK_MOTH, world);
		moth.refreshPositionAndAngles(spawn.getX() + 0.5, spawn.getY() + 0.5, spawn.getZ() + 0.5, dir.asRotation(), 0);
		moth.setVelocity(new Vec3d(dir.getOffsetX() * 0.4, 0, dir.getOffsetZ() * 0.4));
		moth.setHive(world, pos);
		world.spawnEntity(moth);
		world.playLocalSound(null, pos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundSource.BLOCKS, 1, 1);
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, Player player, Hand hand,
			BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND) {
			ItemStack stack = player.getMainHandStack();
			if (stack.getItem().isIn(FabricToolTags.SHEARS) && state.getValue(FULLNESS) == 3) {
				BlocksHelper.setWithUpdate(world, pos, state.with(FULLNESS, 0));
				Direction dir = state.getValue(FACING);
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
