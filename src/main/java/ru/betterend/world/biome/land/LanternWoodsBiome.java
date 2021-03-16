package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class LanternWoodsBiome extends EndBiome {
	public LanternWoodsBiome() {
		super(new BiomeDefinition("lantern_woods")
				.setFogColor(132, 35, 13)
				.setFogDensity(1.1F)
				.setWaterAndFogColor(113, 88, 53)
				.setPlantsColor(237, 122, 66)
				.setSurface(EndBlocks.RUTISCUS)
				.setMusic(EndSounds.MUSIC_FOREST)
				.addFeature(EndFeatures.LUCERNIA)
				.addFeature(EndFeatures.FILALUX)
				.addFeature(EndFeatures.ORANGO)
				.addFeature(EndFeatures.AERIDIUM)
				.addFeature(EndFeatures.LUTEBUS)
				.addFeature(EndFeatures.LAMELLARIUM)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
