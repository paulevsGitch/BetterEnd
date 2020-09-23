package ru.betterend.world.features;

import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public abstract class DefaultFeature extends Feature<DefaultFeatureConfig> {
	public DefaultFeature() {
		super(DefaultFeatureConfig.CODEC);
	}
}
