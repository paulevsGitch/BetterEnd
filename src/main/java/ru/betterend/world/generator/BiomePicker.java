package ru.betterend.world.generator;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import ru.betterend.registry.BiomeRegistry;
import ru.betterend.world.biome.EndBiome;

public class BiomePicker {
	private static final List<EndBiome> BIOMES = Lists.newArrayList();
	private static float maxChance = 0;
	
	public static void addBiome(EndBiome biome) {
		BIOMES.add(biome);
		maxChance = biome.setGenChance(maxChance);
	}
	
	public static EndBiome getBiome(Random random) {
		float chance = random.nextFloat() * maxChance;
		for (EndBiome biome: BIOMES)
			if (biome.canGenerate(chance))
				return biome;
		return BiomeRegistry.END;
	}
	
	public static List<EndBiome> getBiomes() {
		return BIOMES;
	}
}
