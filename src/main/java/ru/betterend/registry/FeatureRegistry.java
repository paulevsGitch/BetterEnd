package ru.betterend.registry;

import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;
import ru.betterend.world.features.SinglePlantFeature;

public class FeatureRegistry {
	// Trees //
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 2);
	
	// Plants //
	public static final EndFeature UMBRELLA_MOSS = new EndFeature("umbrella_moss", new DoublePlantFeature(BlockRegistry.UMBRELLA_MOSS, BlockRegistry.UMBRELLA_MOSS_TALL, 5), 5);
	public static final EndFeature CREEPING_MOSS = new EndFeature("creeping_moss", new SinglePlantFeature(BlockRegistry.CREEPING_MOSS, 5), 5);
	
	// Features //
	public static final EndFeature END_LAKE = EndFeature.makeLakeFeature("end_lake", new EndLakeFeature(), 100);
	
	// Ores //
	public static final EndFeature ENDER_ORE = EndFeature.makeOreFeature("ender_ore", BlockRegistry.ENDER_ORE, 6, 3, 0, 4, 96);
	
	public static void register() {}
}
