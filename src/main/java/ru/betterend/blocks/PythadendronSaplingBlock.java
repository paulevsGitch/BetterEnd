package ru.betterend.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.betterend.blocks.basis.FeatureSaplingBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class PythadendronSaplingBlock extends FeatureSaplingBlock {
	public PythadendronSaplingBlock() {
		super();
	}

	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.PYTHADENDRON_TREE.getFeature();
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.CHORUS_NYLIUM);
	}
}
