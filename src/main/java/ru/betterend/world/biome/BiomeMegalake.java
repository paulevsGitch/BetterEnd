package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.EntityRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.StructureRegistry;

public class BiomeMegalake extends EndBiome {
	public BiomeMegalake() {
		super(new BiomeDefinition("megalake")
				.setFogColor(178, 209, 248)
				.setWaterColor(96, 163, 255)
				.setWaterFogColor(96, 163, 255)
				.setFogDensity(1.75F)
				.setSurface(BlockRegistry.ENDSTONE_DUST)
				.addStructureFeature(StructureRegistry.MEGALAKE)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addFeature(FeatureRegistry.END_LOTUS)
				.addFeature(FeatureRegistry.END_LOTUS_LEAF)
				.addFeature(FeatureRegistry.BUBBLE_CORAL_RARE)
				.addFeature(FeatureRegistry.END_LILY_RARE)
				.addMobSpawn(EntityRegistry.DRAGONFLY, 50, 1, 3)
				.addMobSpawn(EntityRegistry.END_FISH, 50, 3, 8)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
