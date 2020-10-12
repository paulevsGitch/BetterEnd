package ru.betterend.registry;

import ru.betterend.world.features.BlueVineFeature;
import ru.betterend.world.features.PythadendronTreeFeature;
import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.VineFeature;

public class FeatureRegistry {
	// Trees //
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 3);
	public static final EndFeature PYTHADENDRON_TREE = new EndFeature("pythadendron_tree", new PythadendronTreeFeature(), 1);
	
	// Plants //
	public static final EndFeature UMBRELLA_MOSS = new EndFeature("umbrella_moss", new DoublePlantFeature(BlockRegistry.UMBRELLA_MOSS, BlockRegistry.UMBRELLA_MOSS_TALL, 5), 5);
	public static final EndFeature CREEPING_MOSS = new EndFeature("creeping_moss", new SinglePlantFeature(BlockRegistry.CREEPING_MOSS, 5), 5);
	public static final EndFeature BLUE_VINE = new EndFeature("blue_vine", new BlueVineFeature(), 1);
	public static final EndFeature CHORUS_GRASS = new EndFeature("chorus_grass", new SinglePlantFeature(BlockRegistry.CHORUS_GRASS, 4), 4);
	
	public static final EndFeature DENSE_VINE = new EndFeature("dense_vine", new VineFeature(BlockRegistry.DENSE_VINE, 24), 3);
	
	// Features //
	public static final EndFeature END_LAKE = EndFeature.makeLakeFeature("end_lake", new EndLakeFeature(), 4);
	public static final EndFeature RARE_END_LAKE = EndFeature.makeLakeFeature("rare_end_lake", new EndLakeFeature(), 40);
	
	// Ores //
	public static final EndFeature ENDER_ORE = EndFeature.makeOreFeature("ender_ore", BlockRegistry.ENDER_ORE, 6, 3, 0, 4, 96);
	
	public static void register() {}
}
