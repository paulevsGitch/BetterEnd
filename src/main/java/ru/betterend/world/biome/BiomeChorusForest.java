package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class BiomeChorusForest extends EndBiome {
	public BiomeChorusForest() {
		super(new BiomeDefinition("chorus_forest")
				.setFogColor(87, 26, 87)
				.setFogDensity(1.5F)
				.setPlantsColor(122, 45, 122)
				.setSurface(EndBlocks.CHORUS_NYLIUM)
				.setParticles(ParticleTypes.PORTAL, 0.01F)
				.setLoop(EndSounds.AMBIENT_CHORUS_FOREST)
				.setMusic(EndSounds.MUSIC_CHORUS_FOREST)
				.addFeature(EndFeatures.VIOLECITE_LAYER)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.PYTHADENDRON_TREE)
				.addFeature(EndFeatures.PYTHADENDRON_BUSH)
				.addFeature(EndFeatures.PURPLE_POLYPORE_DENSE)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(EndFeatures.CHORUS_GRASS)
				.addFeature(EndFeatures.TAIL_MOSS)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EndEntities.END_SLIME, 5, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
