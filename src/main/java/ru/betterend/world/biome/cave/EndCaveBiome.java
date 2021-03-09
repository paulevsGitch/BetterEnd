package ru.betterend.world.biome.cave;

import java.util.Random;

import net.minecraft.util.collection.WeightedList;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.world.biome.land.BiomeDefinition;
import ru.betterend.world.biome.land.EndBiome;

public class EndCaveBiome extends EndBiome {
	private WeightedList<Feature<?>> floorFeatures = new WeightedList<Feature<?>>();
	private WeightedList<Feature<?>> ceilFeatures = new WeightedList<Feature<?>>();
	
	public EndCaveBiome(BiomeDefinition definition) {
		super(definition.setCaveBiome());
	}
	
	public void addFloorFeature(Feature<?> feature, int weight) {
		floorFeatures.add(feature, weight);
	}
	
	public void addCeilFeature(Feature<?> feature, int weight) {
		ceilFeatures.add(feature, weight);
	}
	
	public Feature<?> getFloorFeature(Random random) {
		return floorFeatures.isEmpty() ? null : floorFeatures.pickRandom(random);
	}
	
	public Feature<?> getCeilFeature(Random random) {
		return ceilFeatures.isEmpty() ? null : ceilFeatures.pickRandom(random);
	}
	
	public float getFloorDensity() {
		return 0;
	}
}
