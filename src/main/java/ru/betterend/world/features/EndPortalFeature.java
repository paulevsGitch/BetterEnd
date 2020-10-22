package ru.betterend.world.features;

import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.GenerationStep;

import ru.betterend.blocks.RunedFlavolite;

public class EndPortalFeature extends EndFeature {
	
	private final RunedFlavolite frameBlock;
	
	public EndPortalFeature(DefaultEndPortalFeature feature, RunedFlavolite frameBlock) {
		this.feature = feature;
		this.featureStep = GenerationStep.Feature.UNDERGROUND_STRUCTURES;
		this.frameBlock = frameBlock;
	}
	
	public EndPortalFeature configure(Direction.Axis axis, boolean active) {
		this.featureConfigured = ((DefaultEndPortalFeature) this.feature).configure(EndPortalFeatureConfig.create(frameBlock, axis, active));
		return this;
	}
}
