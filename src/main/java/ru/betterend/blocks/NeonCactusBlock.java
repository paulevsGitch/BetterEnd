package ru.betterend.blocks;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.BlocksHelper;

public class NeonCactusBlock extends BlockBaseNotFull implements SimpleWaterloggedBlock, IRenderTypeable {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	private static final EnumMap<Direction, VoxelShape> MEDIUM_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Direction, VoxelShape> SMALL_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Axis, VoxelShape> MEDIUM_SHAPES = Maps.newEnumMap(Axis.class);
	private static final EnumMap<Axis, VoxelShape> SMALL_SHAPES = Maps.newEnumMap(Axis.class);
	
	public NeonCactusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(15));
		registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP).setValue(SHAPE, TripleShape.TOP));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, WATERLOGGED, FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		LevelAccessor worldAccess = ctx.getLevel();
		BlockPos blockPos = ctx.getClickedPos();
		return this.defaultBlockState().setValue(WATERLOGGED, worldAccess.getFluidState(blockPos).getType() == Fluids.WATER).setValue(FACING, ctx.getClickedFace());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if ((Boolean) state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		TripleShape shape = state.getValue(SHAPE);
		
		if (shape == TripleShape.BOTTOM) {
			return Shapes.block();
		}
		Direction dir = state.getValue(FACING);
		BlockState next = view.getBlockState(pos.relative(dir));
		if (next.is(this)) {
			Axis axis = dir.getAxis();
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES.get(axis) : SMALL_SHAPES.get(axis);
		}
		else {
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES_OPEN.get(dir) : SMALL_SHAPES_OPEN.get(dir);
		}
	}
	
	static {
		MEDIUM_SHAPES.put(Axis.X, Block.box(0, 2, 2, 16, 14, 14));
		MEDIUM_SHAPES.put(Axis.Y, Block.box(2, 0, 2, 14, 16, 14));
		MEDIUM_SHAPES.put(Axis.Z, Block.box(2, 2, 0, 14, 14, 16));
		
		SMALL_SHAPES.put(Axis.X, Block.box(0, 4, 4, 16, 12, 12));
		SMALL_SHAPES.put(Axis.Y, Block.box(4, 0, 4, 12, 16, 12));
		SMALL_SHAPES.put(Axis.Z, Block.box(4, 4, 0, 12, 12, 16));
		
		MEDIUM_SHAPES_OPEN.put(Direction.UP,    Block.box(2, 0, 2, 14, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.DOWN,  Block.box(2, 2, 2, 14, 16, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.NORTH, Block.box(2, 2, 2, 14, 14, 16));
		MEDIUM_SHAPES_OPEN.put(Direction.SOUTH, Block.box(2, 2, 0, 14, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.WEST,  Block.box(2, 2, 2, 16, 14, 14));
		MEDIUM_SHAPES_OPEN.put(Direction.EAST,  Block.box(0, 2, 2, 14, 14, 14));
		
		SMALL_SHAPES_OPEN.put(Direction.UP,    Block.box(4, 0, 4, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.DOWN,  Block.box(4, 4, 4, 12, 16, 12));
		SMALL_SHAPES_OPEN.put(Direction.NORTH, Block.box(4, 4, 4, 12, 12, 16));
		SMALL_SHAPES_OPEN.put(Direction.SOUTH, Block.box(4, 4, 0, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.WEST,  Block.box(4, 4, 4, 16, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.EAST,  Block.box(0, 4, 4, 12, 12, 12));
	}
}
