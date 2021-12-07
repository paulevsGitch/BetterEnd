package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.EndBiome;

public class CrystalMountainsBiome extends EndBiome.Config {
	public CrystalMountainsBiome() {
		super("crystal_mountains");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.structure(EndStructures.MOUNTAIN.getFeatureConfigured())
			 .plantsColor(255, 133, 211)
			 .surface(EndBlocks.CRYSTAL_MOSS)
			 .music(EndSounds.MUSIC_OPENSPACE)
			 .feature(EndFeatures.ROUND_CAVE)
			 .feature(EndFeatures.CRYSTAL_GRASS)
			 .spawn(EntityType.ENDERMAN, 50, 1, 2);
	}
}
