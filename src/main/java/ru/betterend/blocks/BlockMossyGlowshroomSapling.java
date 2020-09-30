package ru.betterend.blocks;

import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.FeatureRegistry;

public class BlockMossyGlowshroomSapling extends BlockFeatureSapling {
	public BlockMossyGlowshroomSapling() {
		super(7);
	}

	@Override
	protected Feature<DefaultFeatureConfig> getFeature() {
		return FeatureRegistry.MOSSY_GLOWSHROOM.getFeature();
	}
}
