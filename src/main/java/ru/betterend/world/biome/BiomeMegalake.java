package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.BlockRegistry;
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
				.addFeature(FeatureRegistry.BUBBLE_CORAL)
				.addFeature(FeatureRegistry.END_LILY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
