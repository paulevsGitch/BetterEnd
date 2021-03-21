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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.BlocksHelper;

public class NeonCactusBlock extends BlockBaseNotFull implements Waterloggable, IRenderTypeable {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final DirectionProperty FACING = Properties.FACING;
	
	private static final VoxelShape MEDIUM_X = Block.createCuboidShape(0, 2, 2, 16, 14, 14);
	private static final VoxelShape MEDIUM_Y = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
	private static final VoxelShape MEDIUM_Z = Block.createCuboidShape(2, 2, 0, 14, 14, 16);
	
	private static final VoxelShape SMALL_X = Block.createCuboidShape(0, 4, 4, 16, 12, 12);
	private static final VoxelShape SMALL_Y = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape SMALL_Z = Block.createCuboidShape(4, 4, 0, 12, 12, 16);
	
	public NeonCactusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(state -> {
			TripleShape shape = state.get(SHAPE);
			if (shape == TripleShape.TOP) {
				return 15;
			}
			return shape == TripleShape.MIDDLE ? 13 : 10;
		}));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, WATERLOGGED, FACING);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess worldAccess = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		return this.getDefaultState().with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER).with(FACING, ctx.getSide());
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
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if ((Boolean) state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		TripleShape shape = state.get(SHAPE);
		
		if (shape == TripleShape.BOTTOM) {
			return VoxelShapes.fullCube();
		}
		Axis dir = state.get(FACING).getAxis();
		if (shape == TripleShape.MIDDLE) {
			if (dir == Axis.Y) {
				return MEDIUM_Y;
			}
			return dir == Axis.X ? MEDIUM_X : MEDIUM_Z;
		}
		else {
			if (dir == Axis.Y) {
				return SMALL_Y;
			}
			return dir == Axis.X ? SMALL_X : SMALL_Z;
		}
	}
}
