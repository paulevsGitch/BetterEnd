package ru.betterend.registry;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.world.features.BlueVineFeature;
import ru.betterend.world.features.CavePlantFeature;
import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.EndLilyFeature;
import ru.betterend.world.features.EndLotusFeature;
import ru.betterend.world.features.EndLotusLeafFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;
import ru.betterend.world.features.PythadendronBushFeature;
import ru.betterend.world.features.PythadendronTreeFeature;
import ru.betterend.world.features.RoundCave;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.UnderwaterPlantFeature;
import ru.betterend.world.features.VineFeature;

public class FeatureRegistry {
	// Trees //
	public static final EndFeature MOSSY_GLOWSHROOM = new EndFeature("mossy_glowshroom", new MossyGlowshroomFeature(), 3);
	public static final EndFeature PYTHADENDRON_TREE = new EndFeature("pythadendron_tree", new PythadendronTreeFeature(), 2);
	
	// Bushes //
	public static final EndFeature PYTHADENDRON_BUSH = new EndFeature("pythadendron_bush", new PythadendronBushFeature(), 4);
	
	// Plants //
	public static final EndFeature UMBRELLA_MOSS = new EndFeature("umbrella_moss", new DoublePlantFeature(BlockRegistry.UMBRELLA_MOSS, BlockRegistry.UMBRELLA_MOSS_TALL, 5), 5);
	public static final EndFeature CREEPING_MOSS = new EndFeature("creeping_moss", new SinglePlantFeature(BlockRegistry.CREEPING_MOSS, 5), 5);
	public static final EndFeature BLUE_VINE = new EndFeature("blue_vine", new BlueVineFeature(), 1);
	public static final EndFeature CHORUS_GRASS = new EndFeature("chorus_grass", new SinglePlantFeature(BlockRegistry.CHORUS_GRASS, 4), 5);
	public static final EndFeature CAVE_GRASS = new EndFeature("cave_grass", new CavePlantFeature(BlockRegistry.CAVE_GRASS, 7), 5);
	
	public static final EndFeature DENSE_VINE = new EndFeature("dense_vine", new VineFeature(BlockRegistry.DENSE_VINE, 24), 3);
	
	public static final EndFeature BUBBLE_CORAL = new EndFeature("bubble_coral", new UnderwaterPlantFeature(BlockRegistry.BUBBLE_CORAL, 10), 10);
	public static final EndFeature BUBBLE_CORAL_RARE = new EndFeature("bubble_coral_rare", new UnderwaterPlantFeature(BlockRegistry.BUBBLE_CORAL, 3), 2);
	public static final EndFeature END_LILY = new EndFeature("end_lily", new EndLilyFeature(10), 10);
	public static final EndFeature END_LILY_RARE = new EndFeature("end_lily_rare", new EndLilyFeature(3), 1);
	public static final EndFeature END_LOTUS = new EndFeature("end_lotus", new EndLotusFeature(7), 5);
	public static final EndFeature END_LOTUS_LEAF = new EndFeature("end_lotus_leaf", new EndLotusLeafFeature(20), 25);
	
	// Features //
	public static final EndFeature END_LAKE = EndFeature.makeLakeFeature("end_lake", new EndLakeFeature(), 4);
	public static final EndFeature END_LAKE_RARE = EndFeature.makeLakeFeature("end_lake_rare", new EndLakeFeature(), 40);
	public static final EndFeature ROUND_CAVE = EndFeature.makeRawGenFeature("round_cave", new RoundCave(), 2);
	public static final EndFeature ROUND_CAVE_RARE = EndFeature.makeRawGenFeature("round_cave_rare", new RoundCave(), 25);
	
	// Ores //
	public static final EndFeature ENDER_ORE = EndFeature.makeOreFeature("ender_ore", BlockRegistry.ENDER_ORE, 6, 3, 0, 4, 96);
	public static final EndFeature VIOLECITE_LAYER = EndFeature.makeLayerFeature("violecite_layer", BlockRegistry.VIOLECITE, 15, 4, 96, 8);
	public static final EndFeature FLAVOLITE_LAYER = EndFeature.makeLayerFeature("flavolite_layer", BlockRegistry.FLAVOLITE, 12, 4, 96, 6);
	
	public static void registerBiomeFeatures(Identifier id, Biome biome, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		addFeature(FLAVOLITE_LAYER, features);
		addFeature(ENDER_ORE, features);
		addFeature(ROUND_CAVE_RARE, features);
		addFeature(CAVE_GRASS, features);
		
		if (id.getNamespace().equals("minecraft")) {
			if (id.getPath().equals("end_highlands")) {
				addFeature(ROUND_CAVE, features);
			}
		}
	}
	
	private static void addFeature(EndFeature feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		int index = feature.getFeatureStep().ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> {
				return feature.getFeatureConfigured();
			});
		}
		else {
			List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
			newFeature.add(() -> {
				return feature.getFeatureConfigured();
			});
			features.add(newFeature);
		}
	}
	
	public static void register() {}
}
