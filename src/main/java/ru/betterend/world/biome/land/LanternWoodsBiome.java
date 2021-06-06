package ru.betterend.world.biome.land;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class LanternWoodsBiome extends EndBiome {
	public LanternWoodsBiome() {
		super((BiomeDefinition) new BiomeDefinition("lantern_woods")
				.setFogColor(189, 82, 70)
				.setFogDensity(1.1F)
				.setWaterAndFogColor(171, 234, 226)
				.setPlantsColor(254, 85, 57)
				.setSurface(EndBlocks.RUTISCUS)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.addFeature(EndFeatures.END_LAKE_NORMAL)
				.addFeature(EndFeatures.FLAMAEA)
				.addFeature(EndFeatures.LUCERNIA)
				.addFeature(EndFeatures.LUCERNIA_BUSH)
				.addFeature(EndFeatures.FILALUX)
				.addFeature(EndFeatures.AERIDIUM)
				.addFeature(EndFeatures.LAMELLARIUM)
				.addFeature(EndFeatures.BOLUX_MUSHROOM)
				.addFeature(EndFeatures.AURANT_POLYPORE)
				.addFeature(EndFeatures.POND_ANEMONE)
				.addFeature(EndFeatures.CHARNIA_ORANGE)
				.addFeature(EndFeatures.CHARNIA_RED)
				.addFeature(EndFeatures.RUSCUS)
				.addFeature(EndFeatures.RUSCUS_WOOD)
				.addStructureFeature(StructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
