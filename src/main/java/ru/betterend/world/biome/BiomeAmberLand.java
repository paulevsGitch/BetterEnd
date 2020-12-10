package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class BiomeAmberLand extends EndBiome {
	public BiomeAmberLand() {
		super(new BiomeDefinition("amber_land")
				.setFogColor(255, 184, 71)
				.setFogDensity(2.0F)
				.setPlantsColor(219, 115, 38)
				.setWaterAndFogColor(145, 108, 72)
				.setSurface(EndBlocks.AMBER_MOSS)
				.addFeature(EndFeatures.AMBER_ORE)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.HELIX_TREE)
				.addFeature(EndFeatures.LANCELEAF)
				.addFeature(EndFeatures.GLOW_PILLAR)
				.addFeature(EndFeatures.AMBER_GRASS)
				.addFeature(EndFeatures.CHARNIA_ORANGE)
				.addFeature(EndFeatures.CHARNIA_RED)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
