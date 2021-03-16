package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;

public class LanternWoodsBiome extends EndBiome {
	public LanternWoodsBiome() {
		super(new BiomeDefinition("lantern_woods")
				.setFogColor(189, 82, 70)
				.setFogDensity(1.1F)
				.setWaterAndFogColor(171, 234, 226)
				.setPlantsColor(254, 85, 57)
				.setSurface(EndBlocks.RUTISCUS)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.addFeature(EndFeatures.END_LAKE_NORMAL)
				.addFeature(EndFeatures.LUCERNIA)
				.addFeature(EndFeatures.LUCERNIA_BUSH)
				.addFeature(EndFeatures.FILALUX)
				.addFeature(EndFeatures.AERIDIUM)
				.addFeature(EndFeatures.LAMELLARIUM)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
