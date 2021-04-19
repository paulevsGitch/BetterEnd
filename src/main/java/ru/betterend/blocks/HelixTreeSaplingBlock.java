package ru.betterend.blocks;

import net.minecraft.world.level.levelgen.feature.Feature;
import ru.betterend.blocks.basis.FeatureSaplingBlock;
import ru.betterend.registry.EndFeatures;

public class HelixTreeSaplingBlock extends FeatureSaplingBlock {
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.HELIX_TREE.getFeature();
	}
}
