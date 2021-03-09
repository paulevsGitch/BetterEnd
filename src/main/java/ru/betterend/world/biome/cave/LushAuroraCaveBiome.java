package ru.betterend.world.biome.cave;

import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.land.BiomeDefinition;

public class LushAuroraCaveBiome extends EndCaveBiome {
	public LushAuroraCaveBiome() {
		super(new BiomeDefinition("lush_aurora_cave")
				.setFogColor(150, 30, 68)
				.setFogDensity(2.0F)
				.setPlantsColor(108, 25, 46)
				.setWaterAndFogColor(186, 77, 237)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.setSurface(EndBlocks.CAVE_MOSS));
		
		this.addFloorFeature(EndFeatures.BIG_AURORA_CRYSTAL, 1);
		this.addFloorFeature(EndFeatures.CAVE_BUSH, 5);
		this.addFloorFeature(EndFeatures.CAVE_GRASS, 40);
		this.addFloorFeature(EndFeatures.END_STONE_STALAGMITE_CAVEMOSS, 10);
		
		this.addCeilFeature(EndFeatures.CAVE_BUSH, 1);
		this.addCeilFeature(EndFeatures.END_STONE_STALACTITE_CAVEMOSS, 20);
	}
	
	@Override
	public float getFloorDensity() {
		return 0.2F;
	}
	
	@Override
	public float getCeilDensity() {
		return 0.1F;
	}
}
