package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
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
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		BlockState state = world.getBlockState(blockPos);
		return state.getMaterial().isReplaceable() && canPlaceBlock(state, world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		int h = BlocksHelper.downRay(world, blockPos, random.nextInt(maxLength)) - 1;
		if (h > 2) {
			BlockState top = getTopState();
			BlockState middle = getMiggleState();
			BlockState bottom = getBottomState();
			BlocksHelper.setWithoutUpdate(world, blockPos, top);
			for (int i = 1; i < h; i++) {
				BlocksHelper.setWithoutUpdate(world, blockPos.down(i), middle);
			}
			BlocksHelper.setWithoutUpdate(world, blockPos.down(h), bottom);
		}
	}
	
	private boolean canPlaceBlock(BlockState state, StructureWorldAccess world, BlockPos blockPos) {
		if (vine) {
			return ((VineBlock) vineBlock).canGenerate(state, world, blockPos);
		}
		else {
			return vineBlock.canPlaceAt(state, world, blockPos);
		}
	}
	
	private BlockState getTopState() {
		BlockState state = vineBlock.getDefaultState();
		return vine ? state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP) : state;
	}
	
	private BlockState getMiggleState() {
		BlockState state = vineBlock.getDefaultState();
		return vine ? state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE) : state;
	}
	
	private BlockState getBottomState() {
		BlockState state = vineBlock.getDefaultState();
		return vine ? state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM) : state;
	}
}
