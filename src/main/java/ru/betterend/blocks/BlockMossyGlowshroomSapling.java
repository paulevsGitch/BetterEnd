package ru.betterend.blocks;

import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.world.features.DefaultFeature;

public class BlockMossyGlowshroomSapling extends BlockFeatureSapling {
	public BlockMossyGlowshroomSapling() {
		super((DefaultFeature) FeatureRegistry.MOSSY_GLOWSHROOM.getFeature(), 7);
	}
}
