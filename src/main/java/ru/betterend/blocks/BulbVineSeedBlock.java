package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

public class BulbVineSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState up = world.getBlockState(pos.up());
		return up.isIn(EndTags.GEN_TERRAIN) || up.isIn(BlockTags.LOGS) || up.isIn(BlockTags.LEAVES);
	}

	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {
		int h = BlocksHelper.downRay(world, pos, random.nextInt(24)) - 1;
		if (h > 2) {
			BlocksHelper.setWithoutUpdate(world, pos,
					EndBlocks.BULB_VINE.defaultBlockState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
			for (int i = 1; i < h; i++) {
				BlocksHelper.setWithoutUpdate(world, pos.down(i),
						EndBlocks.BULB_VINE.defaultBlockState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
			}
			BlocksHelper.setWithoutUpdate(world, pos.down(h),
					EndBlocks.BULB_VINE.defaultBlockState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		}
	}
}
