package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import ru.betterend.blocks.basis.BaseBlockWithEntity;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

public class EndStoneSmelter extends BaseBlockWithEntity {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty LIT = Properties.LIT;
	public static final String ID = "end_stone_smelter";

	public EndStoneSmelter() {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.GRAY)
				.hardness(4F)
				.resistance(100F)
				.requiresTool()
				.sounds(BlockSoundGroup.STONE));
		this.setDefaultState(this.stateManager.getDefaultState()
											  .with(FACING, Direction.NORTH)
											  .with(LIT, false));
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}

	private void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EndStoneSmelterBlockEntity) {
			player.openHandledScreen((EndStoneSmelterBlockEntity) blockEntity);
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EndStoneSmelterBlockEntity();
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		//TODO
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			double x = pos.getX() + 0.5D;
			double y = pos.getY();
			double z = pos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
			   world.playSound(x, y, z, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = (Direction)state.get(FACING);
			Direction.Axis axis = direction.getAxis();
			double defOffset = random.nextDouble() * 0.6D - 0.3D;
			double offX = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52D : defOffset;
			double offY = random.nextDouble() * 9.0D / 16.0D;
			double offZ = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52D : defOffset;
			world.addParticle(ParticleTypes.SMOKE, x + offX, y + offY, z + offZ, 0.0D, 0.0D, 0.0D);
		}
	}
}
