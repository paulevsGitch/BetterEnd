package ru.betterend.world.biome.cave;

import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.world.biome.BiomeDefinition;

public class EmptyAuroraCaveBiome extends EndCaveBiome {
	public EmptyAuroraCaveBiome() {
		super((BiomeDefinition) new BiomeDefinition("empty_aurora_cave")
				.setFogColor(150, 30, 68)
				.setFogDensity(2.0F)
				.setPlantsColor(108, 25, 46)
				.setWaterAndFogColor(186, 77, 237)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F));
		
		this.addFloorFeature(EndFeatures.BIG_AURORA_CRYSTAL, 1);
		
		this.addCeilFeature(EndFeatures.END_STONE_STALACTITE, 1);
	}
	
	@Override
	public float getFloorDensity() {
		return 0.01F;
	}
	
	@Override
	public float getCeilDensity() {
		return 0.1F;
	}
}
