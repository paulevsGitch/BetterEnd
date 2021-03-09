package ru.betterend.world.biome.cave;

import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.land.BiomeDefinition;

public class EmptyEndCaveBiome extends EndCaveBiome {
	public EmptyEndCaveBiome() {
		super(new BiomeDefinition("empty_end_cave")
				.setFogDensity(2.0F)
				.setMusic(EndSounds.MUSIC_FOREST));
	}
}
