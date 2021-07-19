package ru.betterend.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.blocks.BlockProperties.TripleShape;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.EndBlockProperties.CactusBottom;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.registry.EndBlocks;

import java.util.EnumMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class NeonCactusPlantBlock extends BaseBlockNotFull implements SimpleWaterloggedBlock, RenderLayerProvider, PottablePlant {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	public static final EnumProperty<CactusBottom> CACTUS_BOTTOM = EndBlockProperties.CACTUS_BOTTOM;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	private static final EnumMap<Direction, VoxelShape> BIG_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Direction, VoxelShape> MEDIUM_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Direction, VoxelShape> SMALL_SHAPES_OPEN = Maps.newEnumMap(Direction.class);
	private static final EnumMap<Axis, VoxelShape> BIG_SHAPES = Maps.newEnumMap(Axis.class);
	private static final EnumMap<Axis, VoxelShape> MEDIUM_SHAPES = Maps.newEnumMap(Axis.class);
	private static final EnumMap<Axis, VoxelShape> SMALL_SHAPES = Maps.newEnumMap(Axis.class);
	private static final int MAX_LENGTH = 12;
	
	public NeonCactusPlantBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(15).randomTicks());
		registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false)
												.setValue(FACING, Direction.UP)
												.setValue(SHAPE, TripleShape.TOP));
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
		BlockState state = this.defaultBlockState()
							   .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER)
							   .setValue(FACING, ctx.getClickedFace());
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
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		world.getBlockTicks().scheduleTick(pos, this, 2);
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		Direction dir = state.getValue(FACING);
		BlockState downState = world.getBlockState(pos.relative(dir.getOpposite()));
		if (downState.is(Blocks.END_STONE) || downState.is(EndBlocks.ENDSTONE_DUST)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.SAND);
		}
		else if (downState.is(EndBlocks.END_MOSS)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.MOSS);
		}
		else {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY);
		}
		return state;
	}
	
	@Override
	public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
		if (!blockState.canSurvive(serverLevel, blockPos)) {
			serverLevel.destroyBlock(blockPos, true, null, 1);
		}
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		TripleShape shape = state.getValue(SHAPE);
		Direction dir = state.getValue(FACING);
		BlockState next = view.getBlockState(pos.relative(dir));
		if (next.is(this)) {
			Axis axis = dir.getAxis();
			if (shape == TripleShape.BOTTOM) {
				return BIG_SHAPES.get(axis);
			}
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES.get(axis) : SMALL_SHAPES.get(axis);
		}
		else {
			if (shape == TripleShape.BOTTOM) {
				return BIG_SHAPES_OPEN.get(dir);
			}
			return shape == TripleShape.MIDDLE ? MEDIUM_SHAPES_OPEN.get(dir) : SMALL_SHAPES_OPEN.get(dir);
		}
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction dir = state.getValue(FACING);
		BlockPos supportPos = pos.relative(dir.getOpposite());
		BlockState support = level.getBlockState(supportPos);
		return support.is(this) || support.isFaceSturdy(level, supportPos, dir);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!this.canSurvive(state, world, pos) || random.nextInt(8) > 0) {
			return;
		}
		Direction dir = state.getValue(FACING);
		if (!world.isEmptyBlock(pos.relative(dir))) {
			return;
		}
		int length = getLength(state, world, pos, MAX_LENGTH);
		if (length < 0 || length > MAX_LENGTH - 1) {
			return;
		}
		if (dir.getAxis().isHorizontal()) {
			int horizontal = getHorizontal(state, world, pos, 2);
			if (horizontal > random.nextInt(2)) {
				dir = Direction.UP;
				if (!world.getBlockState(pos.above()).isAir()) {
					return;
				}
			}
		}
		else if (length > 1 && world.getBlockState(pos.relative(dir.getOpposite())).is(this)) {
			Direction side = getSideDirection(world, pos, state, dir, random);
			BlockPos sidePos = pos.relative(side);
			if (world.isEmptyBlock(sidePos)) {
				BlockState placement = state.setValue(SHAPE, TripleShape.TOP)
											.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY)
											.setValue(WATERLOGGED, false)
											.setValue(FACING, side);
				BlocksHelper.setWithoutUpdate(world, sidePos, placement);
			}
		}
		BlockState placement = state.setValue(SHAPE, TripleShape.TOP)
									.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY)
									.setValue(WATERLOGGED, false)
									.setValue(FACING, dir);
		BlocksHelper.setWithoutUpdate(world, pos.relative(dir), placement);
		mutateStem(placement, world, pos, MAX_LENGTH);
	}
	
	public void growPlant(WorldGenLevel world, BlockPos pos, Random random) {
		growPlant(world, pos, random, MHelper.randRange(MAX_LENGTH >> 1, MAX_LENGTH, random));
	}
	
	public void growPlant(WorldGenLevel world, BlockPos pos, Random random, int iterations) {
		BlockState state = defaultBlockState();
		BlockState downState = world.getBlockState(pos.below());
		if (downState.is(Blocks.END_STONE) || downState.is(EndBlocks.ENDSTONE_DUST)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.SAND);
		}
		else if (downState.is(EndBlocks.END_MOSS)) {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.MOSS);
		}
		else {
			state = state.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY);
		}
		BlocksHelper.setWithoutUpdate(world, pos, state);
		List<MutableBlockPos> ends = Lists.newArrayList(pos.mutable());
		for (int i = 0; i < iterations; i++) {
			int count = ends.size();
			for (int n = 0; n < count; n++) {
				if (!growIteration(world, ends.get(n), random, ends, i)) {
					ends.remove(n);
					count--;
					n--;
				}
			}
		}
	}
	
	private boolean growIteration(WorldGenLevel world, MutableBlockPos pos, Random random, List<MutableBlockPos> ends, int length) {
		BlockState state = world.getBlockState(pos);
		if (!state.is(this)) {
			return false;
		}
		Direction dir = state.getValue(FACING);
		if (!world.isEmptyBlock(pos.relative(dir))) {
			return false;
		}
		if (dir.getAxis().isHorizontal()) {
			int horizontal = getHorizontal(state, world, pos, 2);
			if (horizontal > random.nextInt(2)) {
				dir = Direction.UP;
				if (!world.getBlockState(pos.above()).isAir()) {
					return false;
				}
			}
		}
		else if (length > 1 && world.getBlockState(pos.below()).is(this)) {
			Direction side = getSideDirection(world, pos, state, dir, random);
			BlockPos sidePos = pos.relative(side);
			if (world.isEmptyBlock(sidePos)) {
				BlockState placement = state.setValue(SHAPE, TripleShape.TOP)
											.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY)
											.setValue(WATERLOGGED, false)
											.setValue(FACING, side);
				BlocksHelper.setWithoutUpdate(world, sidePos, placement);
				ends.add(sidePos.mutable());
			}
		}
		BlockState placement = state.setValue(SHAPE, TripleShape.TOP)
									.setValue(CACTUS_BOTTOM, CactusBottom.EMPTY)
									.setValue(WATERLOGGED, false)
									.setValue(FACING, dir);
		BlocksHelper.setWithoutUpdate(world, pos.relative(dir), placement);
		mutateStem(placement, world, pos, MAX_LENGTH);
		pos.move(dir);
		return true;
	}
	
	private Direction getSideDirection(WorldGenLevel world, BlockPos pos, BlockState iterState, Direction dir, Random random) {
		MutableBlockPos iterPos = pos.mutable();
		Direction startDir = dir;
		Direction lastDir = null;
		while (iterState.is(this) && startDir.getAxis().isVertical()) {
			startDir = iterState.getValue(FACING);
			if (lastDir == null) {
				for (Direction side : BlocksHelper.HORIZONTAL) {
					BlockState sideState = world.getBlockState(iterPos.relative(side));
					if (sideState.is(this)) {
						Direction sideDir = sideState.getValue(FACING);
						if (sideDir != side) {
							continue;
						}
						lastDir = sideDir;
					}
				}
			}
			iterPos.move(dir);
			iterState = world.getBlockState(iterPos);
		}
		
		Direction side = lastDir == null ? BlocksHelper.randomHorizontal(random) : lastDir.getClockWise();
		if (side.getOpposite() == startDir) {
			side = side.getOpposite();
		}
		return side;
	}
	
	@Override
	public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
		return false;
	}
	
	@Override
	public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
		entity.hurt(DamageSource.CACTUS, 1.0F);
	}
	
	private int getLength(BlockState state, ServerLevel world, BlockPos pos, int max) {
		int length = 0;
		Direction dir = state.getValue(FACING).getOpposite();
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 0; i < max; i++) {
			mut.move(dir);
			state = world.getBlockState(mut);
			if (!state.is(this)) {
				if (!state.is(TagAPI.END_GROUND)) {
					length = -1;
				}
				break;
			}
			dir = state.getValue(FACING).getOpposite();
			length++;
		}
		return length;
	}
	
	private int getHorizontal(BlockState state, WorldGenLevel world, BlockPos pos, int max) {
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
			count++;
		}
		return count;
	}
	
	private void mutateStem(BlockState state, WorldGenLevel world, BlockPos pos, int max) {
		Direction dir = state.getValue(FACING).getOpposite();
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		for (int i = 0; i < max; i++) {
			mut.move(dir);
			state = world.getBlockState(mut);
			if (!state.is(this)) {
				return;
			}
			int size = (i + 2) * 3 / max;
			int src = state.getValue(SHAPE).getIndex();
			dir = state.getValue(FACING).getOpposite();
			if (src < size) {
				TripleShape shape = TripleShape.fromIndex(size);
				BlocksHelper.setWithoutUpdate(world, mut, state.setValue(SHAPE, shape));
			}
		}
	}
	
	static {
		BIG_SHAPES.put(Axis.X, Block.box(0, 2, 2, 16, 14, 14));
		BIG_SHAPES.put(Axis.Y, Block.box(2, 0, 2, 14, 16, 14));
		BIG_SHAPES.put(Axis.Z, Block.box(2, 2, 0, 14, 14, 16));
		
		MEDIUM_SHAPES.put(Axis.X, Block.box(0, 3, 3, 16, 13, 13));
		MEDIUM_SHAPES.put(Axis.Y, Block.box(3, 0, 3, 13, 16, 13));
		MEDIUM_SHAPES.put(Axis.Z, Block.box(3, 3, 0, 13, 13, 16));
		
		SMALL_SHAPES.put(Axis.X, Block.box(0, 4, 4, 16, 12, 12));
		SMALL_SHAPES.put(Axis.Y, Block.box(4, 0, 4, 12, 16, 12));
		SMALL_SHAPES.put(Axis.Z, Block.box(4, 4, 0, 12, 12, 16));
		
		BIG_SHAPES_OPEN.put(Direction.UP, Block.box(2, 0, 2, 14, 14, 14));
		BIG_SHAPES_OPEN.put(Direction.DOWN, Block.box(2, 2, 2, 14, 16, 14));
		BIG_SHAPES_OPEN.put(Direction.NORTH, Block.box(2, 2, 2, 14, 14, 16));
		BIG_SHAPES_OPEN.put(Direction.SOUTH, Block.box(2, 2, 0, 14, 14, 14));
		BIG_SHAPES_OPEN.put(Direction.WEST, Block.box(2, 2, 2, 16, 14, 14));
		BIG_SHAPES_OPEN.put(Direction.EAST, Block.box(0, 2, 2, 14, 14, 14));
		
		MEDIUM_SHAPES_OPEN.put(Direction.UP, Block.box(3, 0, 3, 13, 13, 13));
		MEDIUM_SHAPES_OPEN.put(Direction.DOWN, Block.box(3, 3, 3, 13, 16, 13));
		MEDIUM_SHAPES_OPEN.put(Direction.NORTH, Block.box(3, 3, 3, 13, 13, 16));
		MEDIUM_SHAPES_OPEN.put(Direction.SOUTH, Block.box(3, 3, 0, 13, 13, 13));
		MEDIUM_SHAPES_OPEN.put(Direction.WEST, Block.box(3, 3, 3, 16, 13, 13));
		MEDIUM_SHAPES_OPEN.put(Direction.EAST, Block.box(0, 3, 3, 13, 13, 13));
		
		SMALL_SHAPES_OPEN.put(Direction.UP, Block.box(4, 0, 4, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.DOWN, Block.box(4, 4, 4, 12, 16, 12));
		SMALL_SHAPES_OPEN.put(Direction.NORTH, Block.box(4, 4, 4, 12, 12, 16));
		SMALL_SHAPES_OPEN.put(Direction.SOUTH, Block.box(4, 4, 0, 12, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.WEST, Block.box(4, 4, 4, 16, 12, 12));
		SMALL_SHAPES_OPEN.put(Direction.EAST, Block.box(0, 4, 4, 12, 12, 12));
	}
	
	@Override
	public boolean canPlantOn(Block block) {
		return true;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public String getPottedState() {
		return "bottom=moss,shape=top,facing=up";
	}
}
