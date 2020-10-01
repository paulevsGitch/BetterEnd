package ru.betterend.world.biome;

import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ParticleRegistry;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterColor(119, 227, 250)
				.setWaterFogColor(119, 227, 250)
				.setSurface(BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM)
				.setParticles(ParticleRegistry.GLOWING_SPHERE, 0.001F)
				.addFeature(FeatureRegistry.ENDER_ORE)
				.addFeature(FeatureRegistry.END_LAKE)
				.addFeature(FeatureRegistry.MOSSY_GLOWSHROOM)
				.addFeature(FeatureRegistry.UMBRELLA_MOSS)
				.addFeature(FeatureRegistry.CREEPING_MOSS));
	}
}
