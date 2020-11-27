package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockLantern extends BlockBaseNotFull implements Waterloggable, FluidFillable {
	public static final BooleanProperty IS_FLOOR = BooleanProperty.of("is_floor");
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	public BlockLantern(Block source) {
		this(FabricBlockSettings.copyOf(source).luminance(15));
	}
	
	public BlockLantern(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(IS_FLOOR, WATERLOGGED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction dir = ctx.getSide();
		boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
		if (dir != Direction.DOWN && dir != Direction.UP) {
			if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
			else if (sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
			else {
				return null;
			}
		}
		else if (dir == Direction.DOWN) {
			if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
			else if (sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
			else {
				return null;
			}
		}
		else {
			if (sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP)) {
				return getDefaultState().with(IS_FLOOR, true).with(WATERLOGGED, water);
			}
			else if (sideCoversSmallSquare(worldView, blockPos.up(), Direction.DOWN)) {
				return getDefaultState().with(IS_FLOOR, false).with(WATERLOGGED, water);
			}
			else {
				return null;
			}
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(IS_FLOOR)) {
			return sideCoversSmallSquare(world, pos.down(), Direction.UP);
		}
		else {
			return sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		Boolean water = state.get(WATERLOGGED);
		if (water) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (!canPlaceAt(state, world, pos)) {
			return water ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}
}
