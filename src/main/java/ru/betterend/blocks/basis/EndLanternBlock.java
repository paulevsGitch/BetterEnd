package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FluidFillable;
import net.minecraft.world.level.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;

public class EndLanternBlock extends BlockBaseNotFull implements Waterloggable, FluidFillable {
	public static final BooleanProperty IS_FLOOR = BlockProperties.IS_FLOOR;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public EndLanternBlock(Block source) {
		this(FabricBlockSettings.copyOf(source).luminance(15).nonOpaque());
	}

	public EndLanternBlock(FabricBlockSettings settings) {
		super(settings.nonOpaque());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(IS_FLOOR, WATERLOGGED);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldView worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getBlockPos();
		Direction dir = ctx.getSide();
		boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
		if (dir != Direction.DOWN && dir != Direction.UP) {
			if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			} else if (sideCoversSmallSquare(worldView, blockPos.below(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			} else {
				return null;
			}
		} else if (dir == Direction.DOWN) {
			if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			} else if (sideCoversSmallSquare(worldView, blockPos.below(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			} else {
				return null;
			}
		} else {
			if (sideCoversSmallSquare(worldView, blockPos.below(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			} else if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			} else {
				return null;
			}
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.getValue(IS_FLOOR)) {
			return sideCoversSmallSquare(world, pos.below(), Direction.UP);
		} else {
			return sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		Boolean water = state.getValue(WATERLOGGED);
		if (water) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (!canPlaceAt(state, world, pos)) {
			return water ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.defaultBlockState();
	}
}
