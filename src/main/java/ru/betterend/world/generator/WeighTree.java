package ru.betterend.world.generator;

import java.util.List;

import ru.betterend.world.biome.land.EndBiome;

public class WeighTree {
	private final Node root;
	
	public WeighTree(List<EndBiome> biomes) {
		root = getNode(biomes);
	}
	
	public EndBiome getBiome(float value) {
		return root.getBiome(value);
	}
	
	private Node getNode(List<EndBiome> biomes) {
		int size = biomes.size();
		if (size == 1) {
			return new Leaf(biomes.get(0));
		}
		else if (size == 2) {
			EndBiome first = biomes.get(0);
			return new Branch(first.getGenChance(), new Leaf(first), new Leaf(biomes.get(1)));
		}
		else {
			int index = size >> 1;
			float separator = biomes.get(index).getGenChance();
			Node a = getNode(biomes.subList(0, index + 1));
			Node b = getNode(biomes.subList(index, size));
			return new Branch(separator, a, b);
		}
	}
	
	private abstract class Node {
		abstract EndBiome getBiome(float value);
	}
	
	private class Branch extends Node {
		final float separator;
		final Node min;
		final Node max;
		
		public Branch(float separator, Node min, Node max) {
			this.separator = separator;
			this.min = min;
			this.max = max;
		}

		@Override
		EndBiome getBiome(float value) {
			return value < separator ? min.getBiome(value) : max.getBiome(value);
		}
	}
	
	private class Leaf extends Node {
		final EndBiome biome;
		
		Leaf(EndBiome biome) {
			this.biome = biome;
		}

		@Override
		EndBiome getBiome(float value) {
			return biome;
		}
	}
}
