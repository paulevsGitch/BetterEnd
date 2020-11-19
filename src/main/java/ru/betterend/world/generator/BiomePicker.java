package ru.betterend.world.generator;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.util.Identifier;
import ru.betterend.world.biome.EndBiome;

public class BiomePicker {
	private final Set<Identifier> immutableIDs = Sets.newHashSet();
	private final List<EndBiome> biomes = Lists.newArrayList();
	private float maxChanceUnmutable = 0;
	private float maxChance = 0;
	private int biomeCount = 0;
	private WeighTree tree;
	
	public void addBiome(EndBiome biome) {
		maxChance = biome.mutateGenChance(maxChance);
		immutableIDs.add(biome.getID());
		maxChanceUnmutable = maxChance;
		biomes.add(biome);
		biomeCount ++;
	}
	
	public void addBiomeMutable(EndBiome biome) {
		biomes.add(biome);
		maxChance = biome.mutateGenChance(maxChance);
	}
	
	public void clearMutables() {
		maxChance = maxChanceUnmutable;
		for (int i = biomes.size() - 1; i >= biomeCount; i--)
			biomes.remove(i);
	}
	
	public EndBiome getBiome(Random random) {
		return tree.getBiome(random.nextFloat() * maxChance);
	}
	
	public List<EndBiome> getBiomes() {
		return biomes;
	}
	
	public boolean containsImmutable(Identifier id) {
		return immutableIDs.contains(id);
	}
	
	public void rebuild() {
		tree = new WeighTree(biomes);
	}
}
