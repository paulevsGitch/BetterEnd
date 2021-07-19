package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class BlueVineSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
		int height = MHelper.randRange(2, 5, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height + 1) {
			return;
		}
		BlocksHelper.setWithoutUpdate(
			world,
			pos,
			EndBlocks.BLUE_VINE.defaultBlockState()
							   .setValue(BlockProperties.TRIPLE_SHAPE, BlockProperties.TripleShape.BOTTOM)
		);
		for (int i = 1; i < height; i++) {
			BlocksHelper.setWithoutUpdate(
				world,
				pos.above(i),
				EndBlocks.BLUE_VINE.defaultBlockState()
								   .setValue(BlockProperties.TRIPLE_SHAPE, BlockProperties.TripleShape.MIDDLE)
			);
		}
		BlocksHelper.setWithoutUpdate(
			world,
			pos.above(height),
			EndBlocks.BLUE_VINE.defaultBlockState()
							   .setValue(BlockProperties.TRIPLE_SHAPE, BlockProperties.TripleShape.TOP)
		);
		placeLantern(world, pos.above(height + 1));
	}
	
	private void placeLantern(WorldGenLevel world, BlockPos pos) {
		BlocksHelper.setWithoutUpdate(
			world,
			pos,
			EndBlocks.BLUE_VINE_LANTERN.defaultBlockState().setValue(BlueVineLanternBlock.NATURAL, true)
		);
		for (Direction dir : BlocksHelper.HORIZONTAL) {
			BlockPos p = pos.relative(dir);
			if (world.isEmptyBlock(p)) {
				BlocksHelper.setWithoutUpdate(
					world,
					p,
					EndBlocks.BLUE_VINE_FUR.defaultBlockState().setValue(FurBlock.FACING, dir)
				);
			}
		}
		if (world.isEmptyBlock(pos.above())) {
			BlocksHelper.setWithoutUpdate(
				world,
				pos.above(),
				EndBlocks.BLUE_VINE_FUR.defaultBlockState().setValue(FurBlock.FACING, Direction.UP)
			);
		}
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS) || state.is(EndBlocks.END_MYCELIUM);
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
}
