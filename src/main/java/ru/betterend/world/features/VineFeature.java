package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.util.BlocksHelper;

public class VineFeature extends InvertedScatterFeature {
	private final Block vineBlock;
	private final int maxLength;
	private final boolean vine;
	
	public VineFeature(Block vineBlock, int maxLength) {
		super(6);
		this.vineBlock = vineBlock;
		this.maxLength = maxLength;
		this.vine = vineBlock instanceof VineBlock;
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		BlockState state = world.getBlockState(blockPos);
		return state.getMaterial().isReplaceable() && canPlaceBlock(state, world, blockPos);
	}

	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos blockPos) {
		int h = BlocksHelper.downRay(world, blockPos, random.nextInt(maxLength)) - 1;
		if (h > 2) {
			BlockState top = getTopState();
			BlockState middle = getMiggleState();
			BlockState bottom = getBottomState();
			BlocksHelper.setWithoutUpdate(world, blockPos, top);
			for (int i = 1; i < h; i++) {
				BlocksHelper.setWithoutUpdate(world, blockPos.below(i), middle);
			}
			BlocksHelper.setWithoutUpdate(world, blockPos.below(h), bottom);
		}
	}
	
	private boolean canPlaceBlock(BlockState state, WorldGenLevel world, BlockPos blockPos) {
		if (vine) {
			return ((VineBlock) vineBlock).canGenerate(state, world, blockPos);
		}
		else {
			return vineBlock.canSurvive(state, world, blockPos);
		}
	}
	
	private BlockState getTopState() {
		BlockState state = vineBlock.defaultBlockState();
		return vine ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP) : state;
	}
	
	private BlockState getMiggleState() {
		BlockState state = vineBlock.defaultBlockState();
		return vine ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE) : state;
	}
	
	private BlockState getBottomState() {
		BlockState state = vineBlock.defaultBlockState();
		return vine ? state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM) : state;
	}
}
