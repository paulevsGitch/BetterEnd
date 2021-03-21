package ru.betterend.blocks;

import java.util.EnumMap;

import com.google.common.collect.Maps;

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
	
	private static final EnumMap<Direction, VoxelShape> MEDIUM_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Direction, VoxelShape> SMALL_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Axis, VoxelShape> MEDIUM_SHAPES = Maps.newEnumMap(Axis.class);
	private static final EnumMap<Axis, VoxelShape> SMALL_SHAPES = Maps.newEnumMap(Axis.class);
	
	public NeonCactusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(state -> {
			TripleShape shape = state.get(SHAPE);
			if (shape == TripleShape.TOP) {
				return 15;
			}
			return shape == TripleShape.MIDDLE ? 13 : 10;
		}));
		setDefaultState(getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.UP).with(SHAPE, TripleShape.TOP));
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
		Direction dir = state.get(FACING);
		BlockState next = view.getBlockState(pos.offset(dir));
		if (next.isOf(this)) {
			Axis axis = dir.getAxis();
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES.get(axis) : SMALL_SHAPES.get(axis);
		}
		else {
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES_OPEN.get(dir) : SMALL_SHAPES_OPEN.get(dir);
		}
	}
	
	static {
		MEDIUM_SHAPES.put(Axis.X, Block.createCuboidShape(0, 2, 2, 16, 14, 14));
		MEDIUM_SHAPES.put(Axis.Y, Block.createCuboidShape(2, 0, 2, 14, 16, 14));
		MEDIUM_SHAPES.put(Axis.Z, Block.createCuboidShape(2, 2, 0, 14, 14, 16));
		
		SMALL_SHAPES.put(Axis.X, Block.createCuboidShape(0, 4, 4, 16, 12, 12));
		SMALL_SHAPES.put(Axis.Y, Block.createCuboidShape(4, 0, 4, 12, 16, 12));
		SMALL_SHAPES.put(Axis.Z, Block.createCuboidShape(4, 4, 0, 12, 12, 16));
		
		MEDIUM_SHAPES_OPEN.put(Direction.UP,    Block.createCuboidShape(2, 0, 2, 14, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.DOWN,  Block.createCuboidShape(2, 2, 2, 14, 16, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.NORTH, Block.createCuboidShape(2, 2, 2, 14, 14, 16));
		MEDIUM_SHAPES_OPEN.put(Direction.SOUTH, Block.createCuboidShape(2, 2, 0, 14, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.WEST,  Block.createCuboidShape(2, 2, 2, 16, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.EAST,  Block.createCuboidShape(0, 2, 2, 14, 14, 14));
		
		SMALL_SHAPES_OPEN.put(Direction.UP,    Block.createCuboidShape(4, 0, 4, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.DOWN,  Block.createCuboidShape(4, 4, 4, 12, 16, 12));
		SMALL_SHAPES_OPEN.put(Direction.NORTH, Block.createCuboidShape(4, 4, 4, 12, 12, 16));
		SMALL_SHAPES_OPEN.put(Direction.SOUTH, Block.createCuboidShape(4, 4, 0, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.WEST,  Block.createCuboidShape(4, 4, 4, 16, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.EAST,  Block.createCuboidShape(0, 4, 4, 12, 12, 12));
	}
}
