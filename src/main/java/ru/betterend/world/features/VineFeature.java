package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.util.BlocksHelper;

public class VineFeature extends InvertedScatterFeature {
	private final Block vineBlock;
	private final int maxLength;
	
	public VineFeature(Block vineBlock, int maxLength) {
		super(6);
		this.vineBlock = vineBlock;
		this.maxLength = maxLength;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return world.isAir(blockPos) && vineBlock.canPlaceAt(AIR, world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		int h = BlocksHelper.downRay(world, blockPos, random.nextInt(maxLength)) - 1;
		if (h > 2) {
			BlocksHelper.setWithoutUpdate(world, blockPos, vineBlock.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
			for (int i = 1; i < h; i++) {
				BlocksHelper.setWithoutUpdate(world, blockPos.down(i), vineBlock.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
			}
			BlocksHelper.setWithoutUpdate(world, blockPos.down(h), vineBlock.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		}
	}
}
