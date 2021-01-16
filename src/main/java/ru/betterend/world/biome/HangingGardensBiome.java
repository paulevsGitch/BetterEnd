package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;

public class HangingGardensBiome extends EndBiome {
	public HangingGardensBiome() {
		super(new BiomeDefinition("hanging_gardens")
				.setSurface(EndBlocks.SHADOW_GRASS, EndBlocks.CHORUS_NYLIUM)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setWaterAndFogColor(84, 61, 127)
				.setFoliageColor(71, 45, 120)
				.setFogColor(78, 71, 92)
				.setFogDensity(1.5F)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}