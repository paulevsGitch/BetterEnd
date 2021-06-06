package ru.betterend.blocks.basis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import ru.bclib.util.BlocksHelper;

public abstract class AttachedBlock extends BlockBaseNotFull {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public AttachedBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = this.defaultBlockState();
		LevelReader worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getClickedPos();
		Direction[] directions = ctx.getNearestLookingDirections();
		for (int i = 0; i < directions.length; ++i) {
			Direction direction = directions[i];
			Direction direction2 = direction.getOpposite();
			blockState = (BlockState) blockState.setValue(FACING, direction2);
			if (blockState.canSurvive(worldView, blockPos)) {
				return blockState;
			}
		}
		return null;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = (Direction) state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		return canSupportCenter(world, blockPos, direction) || world.getBlockState(blockPos).is(BlockTags.LEAVES);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		else {
			return state;
		}
	}

	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
}
