package ru.betterend.integration.byg;

import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndBiomes;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.generator.BiomeType;

public class BYGBiomes {
	// Original Biomes
	public static final EndBiome BULBIS_GARDENS = EndBiomes.registerBiome(Integrations.BYG.getKey("bulbis_gardens"), BiomeType.LAND, 1F);
	
	// New Biomes
	public static final EndBiome OLD_BULBIS_GARDENS = EndBiomes.registerSubBiome(new OldBulbisGardens(), BULBIS_GARDENS);
	
	public static void register() {}
}
