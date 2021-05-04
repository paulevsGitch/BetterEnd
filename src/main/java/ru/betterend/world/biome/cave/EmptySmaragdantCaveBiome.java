package ru.betterend.world.biome.cave;

import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.world.biome.BiomeDefinition;

public class EmptySmaragdantCaveBiome extends EndCaveBiome {
	public EmptySmaragdantCaveBiome() {
		super(new BiomeDefinition("empty_smaragdant_cave")
				.setFogColor(0, 253, 182)
				.setFogDensity(2.0F)
				.setPlantsColor(0, 131, 145)
				.setWaterAndFogColor(31, 167, 212)
				.setParticles(EndParticles.SMARAGDANT, 0.001F));
		
		this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL, 1);
		this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL_SHARD, 20);
		
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
