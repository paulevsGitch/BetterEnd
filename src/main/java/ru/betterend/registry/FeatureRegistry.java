package ru.betterend.registry;

import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;

public class FeatureRegistry {
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 1);
	public static final EndFeature END_LAKE = EndFeature.MakeLakeFeature("end_lake", new EndLakeFeature(), 100);
	
	public static void register() {}
}
