package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBase;

public class BlockEndLotusStem extends BlockBase implements Waterloggable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape VSHAPE = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	
	public BlockEndLotusStem() {
		super(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));
		this.setDefaultState(getDefaultState().with(WATERLOGGED, false).with(SHAPE, TripleShape.MIDDLE));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return VSHAPE;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, SHAPE);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess worldAccess = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		return this.getDefaultState().with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if ((Boolean) state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}
}
