package ru.betterend.world.biome;

import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import ru.betterend.registry.FeatureRegistry;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterColor(119, 227, 250)
				.setWaterFogColor(119, 227, 250)
				.addFeature(FeatureRegistry.STONE_SPIRAL)
				.addFeature(Feature.LAKES, ConfiguredFeatures.LAKE_WATER));
	}
}
