package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class DryShrublandBiome extends EndBiome {
	public DryShrublandBiome() {
		super(new BiomeDefinition("dry_shrubland").setFogColor(132, 35, 13).setFogDensity(1.2F)
				.setWaterAndFogColor(113, 88, 53).setPlantsColor(237, 122, 66).setSurface(EndBlocks.RUTISCUS)
				.setMusic(EndSounds.MUSIC_OPENSPACE).addFeature(EndFeatures.ORANGO).addFeature(EndFeatures.AERIDIUM)
				.addFeature(EndFeatures.LUTEBUS).addFeature(EndFeatures.LAMELLARIUM)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY).addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
