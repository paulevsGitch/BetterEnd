package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;

public class CavePumpkinVineBlock extends EndPlantWithAgeBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 16, 12);

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.up());
		return isTerrain(down);
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		BlockState down = world.getBlockState(pos.below());
		if (down.getMaterial().isReplaceable()
				|| (down.is(EndBlocks.CAVE_PUMPKIN) && down.get(BlockProperties.SMALL))) {
			if (age < 3) {
				world.setBlockAndUpdate(pos, state.with(AGE, age + 1));
			}
			if (age == 2) {
				world.setBlockAndUpdate(pos.below(),
						EndBlocks.CAVE_PUMPKIN.defaultBlockState().with(BlockProperties.SMALL, true));
			} else if (age == 3) {
				world.setBlockAndUpdate(pos.below(), EndBlocks.CAVE_PUMPKIN.defaultBlockState());
			}
		}
	}

	@Override
	public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		state = super.updateShape(state, facing, neighborState, world, pos, neighborPos);
		if (state.is(this) && state.getValue(BlockProperties.AGE) > 1) {
			BlockState down = world.getBlockState(pos.below());
			if (!down.is(EndBlocks.CAVE_PUMPKIN)) {
				state = state.with(BlockProperties.AGE, 1);
			}
		}
		return state;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}
}
