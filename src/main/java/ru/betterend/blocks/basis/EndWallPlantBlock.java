package ru.betterend.blocks.basis;

import java.util.EnumMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalFacingBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.util.BlocksHelper;

public class EndWallPlantBlock extends EndPlantBlock {
	private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH,
			Block.createCuboidShape(1, 1, 8, 15, 15, 16), Direction.SOUTH, Block.createCuboidShape(1, 1, 0, 15, 15, 8),
			Direction.WEST, Block.createCuboidShape(8, 1, 1, 16, 15, 15), Direction.EAST,
			Block.createCuboidShape(0, 1, 1, 8, 15, 15)));
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public EndWallPlantBlock() {
		this(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.GRASS)
				.breakByHand(true).noCollision());
	}

	public EndWallPlantBlock(int light) {
		this(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.GRASS)
				.luminance(light).breakByHand(true).noCollision());
	}

	public EndWallPlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = (Direction) state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return isSupport(world, blockPos, blockState, direction);
	}

	public boolean isSupport(WorldView world, BlockPos pos, BlockState blockState, Direction direction) {
		return blockState.getMaterial().isSolid() && blockState.isSideSolidFullSquare(world, pos, direction);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = this.defaultBlockState();
		WorldView worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getBlockPos();
		Direction[] directions = ctx.getPlacementDirections();
		for (int i = 0; i < directions.length; ++i) {
			Direction direction = directions[i];
			if (direction.getAxis().isHorizontal()) {
				Direction direction2 = direction.getOpposite();
				blockState = blockState.with(FACING, direction2);
				if (blockState.canPlaceAt(worldView, blockPos)) {
					return blockState;
				}
			}
		}
		return null;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
}
