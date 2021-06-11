package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

import java.util.Random;

public class LumecornSeedBlock extends EndPlantWithAgeBlock {

	@Override
	public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
		EndFeatures.LUMECORN.getFeature().place(world, null, random, pos, null);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS);
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
}
