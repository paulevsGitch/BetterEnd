package ru.betterend.blocks;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.FeatureRegistry;

public class BlockMossyGlowshroomSapling extends BlockFeatureSapling {
	public BlockMossyGlowshroomSapling() {
		super(7);
	}

	@Override
	protected ConfiguredFeature<?,?> getFeature() {
		return FeatureRegistry.MOSSY_GLOWSHROOM.getFeatureConfigured();
	}
}
