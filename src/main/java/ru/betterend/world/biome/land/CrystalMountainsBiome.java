package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;

public class CrystalMountainsBiome extends EndBiome {
	public CrystalMountainsBiome() {
		super(new BiomeDefinition("crystal_mountains")
				.setPlantsColor(255, 133, 211)
				.setSurface(EndBlocks.CRYSTAL_MOSS)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.addStructureFeature(EndStructures.MOUNTAIN)
				.addFeature(EndFeatures.ROUND_CAVE)
				.addFeature(EndFeatures.CRYSTAL_GRASS)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
