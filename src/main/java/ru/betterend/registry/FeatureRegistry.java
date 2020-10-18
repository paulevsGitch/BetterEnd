package ru.betterend.registry;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.world.features.BlueVineFeature;
import ru.betterend.world.features.DoublePlantFeature;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.EndLakeFeature;
import ru.betterend.world.features.EndLilyFeature;
import ru.betterend.world.features.MossyGlowshroomFeature;
import ru.betterend.world.features.PythadendronBushFeature;
import ru.betterend.world.features.PythadendronTreeFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.UnderwaterPlantFeature;
import ru.betterend.world.features.VineFeature;
import ru.betterend.world.structures.EndStructureFeature;
import ru.betterend.world.structures.features.StructureMegaLake;

public class FeatureRegistry {
	private final static List<EndFeature> GLOBAL_FEATURES = Lists.newArrayList();
	
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
	
	public static final EndFeature DENSE_VINE = new EndFeature("dense_vine", new VineFeature(BlockRegistry.DENSE_VINE, 24), 3);
	
	public static final EndFeature BUBBLE_CORAL = new EndFeature("bubble_coral", new UnderwaterPlantFeature(BlockRegistry.BUBBLE_CORAL, 4), 20);
	public static final EndFeature END_LILY = new EndFeature("end_lily", new EndLilyFeature(5), 20);
	
	// Features //
	public static final EndFeature END_LAKE = EndFeature.makeLakeFeature("end_lake", new EndLakeFeature(), 4);
	public static final EndFeature RARE_END_LAKE = EndFeature.makeLakeFeature("rare_end_lake", new EndLakeFeature(), 40);
	
	// Ores //
	public static final EndFeature ENDER_ORE = EndFeature.makeOreFeature("ender_ore", BlockRegistry.ENDER_ORE, 6, 3, 0, 4, 96);
	public static final EndFeature VIOLECITE_LAYER = EndFeature.makeLayerFeature("violecite_layer", BlockRegistry.VIOLECITE, 15, 4, 96, 8);
	public static final EndFeature FLAVOLITE_LAYER = EndFeature.makeLayerFeature("flavolite_layer", BlockRegistry.FLAVOLITE, 12, 4, 96, 6);
	
	// Structures //
	public static final EndStructureFeature MEGALAKE = new EndStructureFeature("megalake", new StructureMegaLake(), Feature.RAW_GENERATION, 4, 1);
	
	public static void registerGlobals(List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		GLOBAL_FEATURES.forEach(feature -> {
			int index = feature.getFeatureStep().ordinal();
			if (features.size() > index) {
				features.get(index).add(() -> {
					return feature.getFeatureConfigured();
				});
			} else {
				List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
				newFeature.add(() -> {
					return feature.getFeatureConfigured();
				});
				features.add(newFeature);
			}
		});
	}
	
	public static void register() {}
	
	static {
		GLOBAL_FEATURES.add(FLAVOLITE_LAYER);
		GLOBAL_FEATURES.add(ENDER_ORE);
	}
}
