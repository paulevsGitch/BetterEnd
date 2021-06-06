package ru.betterend.world.biome.land;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class AmberLandBiome extends EndBiome {
	public AmberLandBiome() {
		super((BiomeDefinition) new BiomeDefinition("amber_land")
				.setFogColor(255, 184, 71)
				.setFogDensity(2.0F)
				.setPlantsColor(219, 115, 38)
				.setWaterAndFogColor(145, 108, 72)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setParticles(EndParticles.AMBER_SPHERE, 0.001F)
				.setSurface(EndBlocks.AMBER_MOSS)
				.addFeature(EndFeatures.AMBER_ORE)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.HELIX_TREE)
				.addFeature(EndFeatures.LANCELEAF)
				.addFeature(EndFeatures.GLOW_PILLAR)
				.addFeature(EndFeatures.AMBER_GRASS)
				.addFeature(EndFeatures.AMBER_ROOT)
				.addFeature(EndFeatures.BULB_MOSS)
				.addFeature(EndFeatures.BULB_MOSS_WOOD)
				.addFeature(EndFeatures.CHARNIA_ORANGE)
				.addFeature(EndFeatures.CHARNIA_RED)
				.addStructureFeature(StructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4)
				.addMobSpawn(EndEntities.END_SLIME, 30, 1, 2));
	}
}
