package ru.betterend.world.biome.cave;

import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.BiomeDefinition;

public class EmptyEndCaveBiome extends EndCaveBiome {
	public EmptyEndCaveBiome() {
		super(new BiomeDefinition("empty_end_cave").setFogDensity(2.0F));
		this.addFloorFeature(EndFeatures.END_STONE_STALAGMITE, 1);
		this.addCeilFeature(EndFeatures.END_STONE_STALACTITE, 1);
	}
	
	@Override
	public float getFloorDensity() {
		return 0.1F;
	}
	
	@Override
	public float getCeilDensity() {
		return 0.1F;
	}
}
