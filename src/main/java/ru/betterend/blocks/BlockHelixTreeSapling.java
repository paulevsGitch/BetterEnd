package ru.betterend.blocks;

import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.EndFeatures;

public class BlockHelixTreeSapling extends BlockFeatureSapling {
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.HELIX_TREE.getFeature();
	}
}
