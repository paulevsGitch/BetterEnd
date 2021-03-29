package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class GlowingGrasslandsBiome extends EndBiome {
	public GlowingGrasslandsBiome() {
		super(new BiomeDefinition("glowing_grasslands")
				.setFogColor(99, 228, 247)
				.setFogDensity(1.3F)
				.setParticles(EndParticles.FIREFLY, 0.001F)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.setLoop(EndSounds.AMBIENT_GLOWING_GRASSLANDS)
				.setSurface(EndBlocks.END_MOSS)
				.setWaterAndFogColor(92, 250, 230)
				.setPlantsColor(73, 210, 209)
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
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
