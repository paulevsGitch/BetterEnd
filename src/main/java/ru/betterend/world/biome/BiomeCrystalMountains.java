package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.StructureRegistry;

public class BiomeCrystalMountains extends EndBiome {
	public BiomeCrystalMountains() {
		super(new BiomeDefinition("crystal_mountains")
				.addStructureFeature(StructureRegistry.MOUNTAIN)
				.addFeature(FeatureRegistry.ROUND_CAVE)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
