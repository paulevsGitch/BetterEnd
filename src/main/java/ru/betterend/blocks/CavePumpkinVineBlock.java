package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
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
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		BlockState down = world.getBlockState(pos.down());
		if (down.getMaterial().isReplaceable() || (down.isOf(EndBlocks.CAVE_PUMPKIN) && down.get(BlockProperties.SMALL))) {
			if (age < 3) {
				world.setBlockState(pos, state.with(AGE, age + 1));
			}
			if (age == 2) {
				world.setBlockState(pos.down(), EndBlocks.CAVE_PUMPKIN.getDefaultState().with(BlockProperties.SMALL, true));
			}
			else if (age == 3) {
				world.setBlockState(pos.down(), EndBlocks.CAVE_PUMPKIN.getDefaultState());
			}
		}
	}

	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		state = super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
		if (state.isOf(this) && state.get(BlockProperties.AGE) > 1) {
			BlockState down = world.getBlockState(pos.down());
			if (!down.isOf(EndBlocks.CAVE_PUMPKIN)) {
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
