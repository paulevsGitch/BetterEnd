package ru.betterend.integration.byg;

import ru.betterend.integration.Integrations;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class OldBulbisGardens extends EndBiome {
	public OldBulbisGardens() {
		super(new BiomeDefinition("old_bulbis_gardens")
				.setSurface(Integrations.BYG.getBlock("bulbis_stem")));
	}
}
