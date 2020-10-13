package ru.betterend.blocks;

import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.FeatureRegistry;

public class BlockMossyPythadendronSapling extends BlockFeatureSapling {
	public BlockMossyPythadendronSapling() {
		super();
	}

	@Override
	protected Feature<?> getFeature() {
		return FeatureRegistry.PYTHADENDRON_TREE.getFeature();
	}
}
