package ru.betterend.registry;

import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.MountainFeature;

public class DefaultBiomeFeatures {
	public static final EndFeature MOUNTAINS = EndFeature.makeChunkFeature("mountains", new MountainFeature());
}
