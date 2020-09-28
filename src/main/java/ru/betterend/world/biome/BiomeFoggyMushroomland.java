package ru.betterend.world.biome;

import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.FeatureRegistry;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterColor(119, 227, 250)
				.setWaterFogColor(119, 227, 250)
				.setSurface(BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM)
				.addFeature(FeatureRegistry.END_LAKE)
				.addFeature(FeatureRegistry.MOSSY_GLOWSHROOM));
	}
}
