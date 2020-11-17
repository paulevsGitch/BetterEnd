package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;

public class BiomeAmberLand extends EndBiome {
	public BiomeAmberLand() {
		super(new BiomeDefinition("amber_land")
				.setFogColor(87, 26, 87)
				.setFogDensity(2.0F)
				.setPlantsColor(122, 45, 122)
				.setSurface(EndBlocks.END_MOSS)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
