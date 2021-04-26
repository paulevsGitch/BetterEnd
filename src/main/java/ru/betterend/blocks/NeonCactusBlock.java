package ru.betterend.blocks;

import java.util.EnumMap;
import java.util.Random;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
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
import ru.betterend.blocks.BlockProperties.CactusBottom;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

public class NeonCactusBlock extends BlockBaseNotFull implements SimpleWaterloggedBlock, IRenderTypeable {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	public static final EnumProperty<CactusBottom> CACTUS_BOTTOM = BlockProperties.CACTUS_BOTTOM;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	private static final EnumMap<Direction, VoxelShape> MEDIUM_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Direction, VoxelShape> SMALL_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Axis, VoxelShape> MEDIUM_SHAPES = Maps.newEnumMap(Axis.class);
	private static final EnumMap<Axis, VoxelShape> SMALL_SHAPES = Maps.newEnumMap(Axis.class);
	
	public NeonCactusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(15).randomTicks());
		registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP).setValue(SHAPE, TripleShape.TOP));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, CACTUS_BOTTOM, WATERLOGGED, FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		LevelAccessor world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Direction dir = ctx.getClickedFace();
		BlockState down = world.getBlockState(pos.relative(dir.getOpposite()));
		BlockState state = this.defaultBlockState().setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER).setValue(FACING, ctx.getClickedFace());
		if (down.is(Blocks.END_STONE) || down.is(EndBlocks.ENDSTONE_DUST)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.SAND);
		}
		else if (down.is(EndBlocks.END_MOSS)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.MOSS);
		}
		else {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY);
		}
		return state;
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
		Direction dir = state.getValue(FACING);
		BlockState down = world.getBlockState(pos.relative(dir.getOpposite()));
		if (down.is(Blocks.END_STONE) || down.is(EndBlocks.ENDSTONE_DUST)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.SAND);
		}
		else if (down.is(EndBlocks.END_MOSS)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.MOSS);
		}
		else {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY);
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
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		Direction dir = state.getValue(FACING);
		if (!world.getBlockState(pos.relative(dir)).isAir() || world.getBlockState(pos.above()).is(this)) {
			return;
		}
		int length = getLength(state, world, pos, 10);
		if (length < 0 || length > 9) {
			return;
		}
		int horizontal = getHorizontal(state, world, pos, 5);
		if (horizontal > random.nextInt(2)) {
			dir = Direction.UP;
			if (!world.getBlockState(pos.above()).isAir()) {
				return;
			}
		}
		BlockState placement = state.setValue(SHAPE, TripleShape.TOP).setValue(CACTUS_BOTTOM, CactusBottom.EMPTY).setValue(WATERLOGGED, false).setValue(FACING, dir);
		BlocksHelper.setWithoutUpdate(world, pos.relative(dir), placement);
		mutateStem(placement, world, pos, 10);
	}
	
	private int getLength(BlockState state, ServerLevel world, BlockPos pos, int max) {
		int length = 0;
		Direction dir = state.getValue(FACING).getOpposite();
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 0; i < max; i++) {
			mut.move(dir);
			state = world.getBlockState(mut);
			if (!state.is(this)) {
				if (!state.is(EndTags.END_GROUND)) {
					length = -1;
				}
				break;
			}
			dir = state.getValue(FACING).getOpposite();
			length ++;
		}
		return length;
	}
	
	private int getHorizontal(BlockState state, ServerLevel world, BlockPos pos, int max) {
		int count = 0;
		Direction dir = state.getValue(FACING).getOpposite();
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 0; i < max; i++) {
			mut.move(dir);
			state = world.getBlockState(mut);
			if (!state.is(this)) {
				break;
			}
			dir = state.getValue(FACING).getOpposite();
			if (dir.getStepY() != 0) {
				break;
			}
			count ++;
		}
		return count;
	}
	
	private void mutateStem(BlockState state, ServerLevel world, BlockPos pos, int max) {
		Direction dir = state.getValue(FACING).getOpposite();
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 0; i < max; i++) {
			mut.move(dir);
			state = world.getBlockState(mut);
			if (!state.is(this)) {
				return;
			}
			int size = i * 3 / max;
			int src = state.getValue(SHAPE).getIndex();
			if (src < size) {
				TripleShape shape = TripleShape.fromIndex(size);
				BlocksHelper.setWithoutUpdate(world, pos, state.setValue(SHAPE, shape));
			}
			else {
				return;
			}
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
