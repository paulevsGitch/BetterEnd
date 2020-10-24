package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.EntityRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.SoundRegistry;

public class BiomeChorusForest extends EndBiome {
	public BiomeChorusForest() {
		super(new BiomeDefinition("chorus_forest")
				.setFogColor(87, 26, 87)
				.setFogDensity(1.5F)
				.setPlantsColor(122, 45, 122)
				.setSurface(BlockRegistry.CHORUS_NYLIUM)
				.setParticles(ParticleTypes.PORTAL, 0.01F)
				.setLoop(SoundRegistry.AMBIENT_CHORUS_FOREST)
				.setMusic(SoundRegistry.MUSIC_CHORUS_FOREST)
				.addFeature(FeatureRegistry.VIOLECITE_LAYER)
				.addFeature(FeatureRegistry.END_LAKE_RARE)
				.addFeature(FeatureRegistry.PYTHADENDRON_TREE)
				.addFeature(FeatureRegistry.PYTHADENDRON_BUSH)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.addFeature(FeatureRegistry.CHORUS_GRASS)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityRegistry.END_SLIME, 5, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
