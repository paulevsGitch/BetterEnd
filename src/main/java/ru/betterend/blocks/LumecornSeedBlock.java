package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class LumecornSeedBlock extends EndPlantWithAgeBlock {
	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {
		EndFeatures.LUMECORN.getFeature().generate(world, null, random, pos, null);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.END_MOSS);
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}
}
