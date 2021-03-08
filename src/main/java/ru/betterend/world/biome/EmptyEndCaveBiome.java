package ru.betterend.world.biome;

import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;

public class EmptyEndCaveBiome extends EndCaveBiome {
	public EmptyEndCaveBiome() {
		super(new BiomeDefinition("empty_end_cave")
				.setFogColor(255, 184, 71)
				.setFogDensity(2.0F)
				.setPlantsColor(219, 115, 38)
				.setWaterAndFogColor(145, 108, 72)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setParticles(EndParticles.AMBER_SPHERE, 0.001F)
				.setSurface(EndBlocks.AMBER_MOSS));
	}
}
