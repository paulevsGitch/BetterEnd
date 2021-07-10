package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.EndBiome;

public class CrystalMountainsBiome extends EndBiome {
	public CrystalMountainsBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("crystal_mountains")).addStructureFeature(EndStructures.MOUNTAIN.getFeatureConfigured()).setPlantsColor(255, 133, 211).setSurface(EndBlocks.CRYSTAL_MOSS).setMusic(EndSounds.MUSIC_OPENSPACE).addFeature(EndFeatures.ROUND_CAVE).addFeature(EndFeatures.CRYSTAL_GRASS).addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
