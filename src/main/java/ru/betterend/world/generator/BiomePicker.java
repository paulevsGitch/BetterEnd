package ru.betterend.world.generator;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ru.betterend.registry.BiomeRegistry;
import ru.betterend.world.biome.EndBiome;

public class BiomePicker {
	private final List<EndBiome> biomes = Lists.newArrayList();
	private float maxChance = 0;
	
	public void addBiome(EndBiome biome) {
		biomes.add(biome);
		maxChance = biome.setGenChance(maxChance);
	}
	
	public EndBiome getBiome(Random random) {
		float chance = random.nextFloat() * maxChance;
		for (EndBiome biome: biomes)
			if (biome.canGenerate(chance))
				return biome;
		return BiomeRegistry.END;
	}
	
	public List<EndBiome> getBiomes() {
		return biomes;
	}
}
