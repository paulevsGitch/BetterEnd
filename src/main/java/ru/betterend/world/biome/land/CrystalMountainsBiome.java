package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class CrystalMountainsBiome extends EndBiome {
	public CrystalMountainsBiome() {
		super((BiomeDefinition) new BiomeDefinition("crystal_mountains")
				.addStructureFeature(EndStructures.MOUNTAIN)
				.setPlantsColor(255, 133, 211)
				.setSurface(EndBlocks.CRYSTAL_MOSS)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.addFeature(EndFeatures.ROUND_CAVE)
				.addFeature(EndFeatures.CRYSTAL_GRASS)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
