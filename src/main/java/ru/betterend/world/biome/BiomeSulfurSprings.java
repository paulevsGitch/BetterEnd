package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;

public class BiomeSulfurSprings extends EndBiome {
	public BiomeSulfurSprings() {
		super(new BiomeDefinition("sulfur_springs")
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
