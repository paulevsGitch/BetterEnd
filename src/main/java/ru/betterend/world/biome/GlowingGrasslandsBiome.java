package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;

public class GlowingGrasslandsBiome extends EndBiome {
	public GlowingGrasslandsBiome() {
		super(new BiomeDefinition("glowing_grasslands")
				.setFogColor(99, 228, 247)
				.setFogDensity(1.3F)
				.setParticles(EndParticles.FIREFLY, 0.001F)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.setSurface(EndBlocks.END_MOSS)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.LUMECORN)
				.addFeature(EndFeatures.BLOOMING_COOKSONIA)
				.addFeature(EndFeatures.SALTEAGO)
				.addFeature(EndFeatures.VAIOLUSH_FERN)
				.addFeature(EndFeatures.FRACTURN)
				.addFeature(EndFeatures.UMBRELLA_MOSS_RARE)
				.addFeature(EndFeatures.CREEPING_MOSS_RARE)
				.addFeature(EndFeatures.TWISTED_UMBRELLA_MOSS_RARE)
				.addFeature(EndFeatures.CHARNIA_CYAN)
				.addFeature(EndFeatures.CHARNIA_GREEN)
				.addFeature(EndFeatures.CHARNIA_LIGHT_BLUE)
				.addFeature(EndFeatures.CHARNIA_RED_RARE)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
