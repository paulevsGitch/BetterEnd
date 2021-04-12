package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.util.BlocksHelper;

public abstract class AttachedBlock extends BlockBaseNotFull {
	public static final DirectionProperty FACING = Properties.FACING;

	public AttachedBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties settings) {
		super(settings);
		this.setDefaultState(this.defaultBlockState().with(FACING, Direction.UP));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = this.defaultBlockState();
		WorldView worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getBlockPos();
		Direction[] directions = ctx.getPlacementDirections();
		for (int i = 0; i < directions.length; ++i) {
			Direction direction = directions[i];
			Direction direction2 = direction.getOpposite();
			blockState = (BlockState) blockState.with(FACING, direction2);
			if (blockState.canPlaceAt(worldView, blockPos)) {
				return blockState;
			}
		}
		return null;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = (Direction) state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		return sideCoversSmallSquare(world, blockPos, direction)
				|| world.getBlockState(blockPos).isIn(BlockTags.LEAVES);
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
