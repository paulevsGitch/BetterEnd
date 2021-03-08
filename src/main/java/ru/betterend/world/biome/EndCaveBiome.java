package ru.betterend.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.world.gen.feature.Feature;

public class EndCaveBiome extends EndBiome {
	private List<Feature<?>> floorFeatures = Lists.newArrayList();
	private List<Feature<?>> ceilFeatures = Lists.newArrayList();
	
	public EndCaveBiome(BiomeDefinition definition) {
		super(definition.setCaveBiome());
	}
	
	public void addFloorFeature(Feature<?> feature) {
		floorFeatures.add(feature);
	}
	
	public void addCeilFeature(Feature<?> feature) {
		ceilFeatures.add(feature);
	}
	
	public Feature<?> getFloorFeature(Random random) {
		return floorFeatures.isEmpty() ? null : floorFeatures.get(random.nextInt(floorFeatures.size()));
	}
	
	public Feature<?> getCeilFeature(Random random) {
		return ceilFeatures.isEmpty() ? null : ceilFeatures.get(random.nextInt(ceilFeatures.size()));
	}
}
