package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class ChorusForestBiome extends EndBiome {
	public ChorusForestBiome() {
		super(new BiomeDefinition("chorus_forest")
				.setFogColor(87, 26, 87)
				.setFogDensity(1.5F)
				.setPlantsColor(122, 45, 122)
				.setWaterAndFogColor(73, 30, 73)
				.setSurface(EndBlocks.CHORUS_NYLIUM)
				.setParticles(ParticleTypes.PORTAL, 0.01F)
				.setLoop(EndSounds.AMBIENT_CHORUS_FOREST)
				.setMusic(EndSounds.MUSIC_DARK)
				.addFeature(EndFeatures.VIOLECITE_LAYER)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.PYTHADENDRON_TREE)
				.addFeature(EndFeatures.PYTHADENDRON_BUSH)
				.addFeature(EndFeatures.PURPLE_POLYPORE)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(EndFeatures.CHORUS_GRASS)
				.addFeature(EndFeatures.TAIL_MOSS)
				.addFeature(EndFeatures.TAIL_MOSS_WOOD)
				.addFeature(EndFeatures.CHARNIA_PURPLE)
				.addFeature(EndFeatures.CHARNIA_RED_RARE)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EndEntities.END_SLIME, 5, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
