package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;

public class BlossomingSpires extends EndBiome {
	public BlossomingSpires() {
		super(new BiomeDefinition("blossoming_spires")
				.setFogColor(87, 26, 87)
				.setFogDensity(2.0F)
				.setPlantsColor(122, 45, 122)
				.setSurface(EndBlocks.END_MOSS)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
