package ru.betterend.registry;

import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;
import ru.betterend.world.features.StoneSpiralFeature;

public class FeatureRegistry {
	public static final EndFeature STONE_SPIRAL = new EndFeature("stone_spiral", new StoneSpiralFeature(), 2);
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 1);
	public static final EndFeature END_LAKE = EndFeature.MakeRawGenFeature("end_lake", new EndLakeFeature(), 100);
	
	public static void register() {}
}
