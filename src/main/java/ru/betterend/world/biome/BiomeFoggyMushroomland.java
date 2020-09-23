package ru.betterend.world.biome;

import ru.betterend.registry.FeatureRegistry;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.addFeature(FeatureRegistry.STONE_SPIRAL));
	}
}
