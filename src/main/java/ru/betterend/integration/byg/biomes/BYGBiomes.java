package ru.betterend.integration.byg.biomes;

import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndBiomes;
import ru.betterend.world.biome.EndBiome;

public class BYGBiomes {
	// New Biomes
	public static final EndBiome OLD_BULBIS_GARDENS = EndBiomes.registerSubBiomeIntegration(new OldBulbisGardens());
	public static final EndBiome NIGHTSHADE_REDWOODS = EndBiomes.registerSubBiomeIntegration(new NightshadeRedwoods());
	//public static final EndBiome ETHERIAL_GROVE = EndBiomes.registerSubBiomeIntegration(new EterialGrove());

	public static void register() {
		System.out.println("Registered " + OLD_BULBIS_GARDENS);
	}

	public static void addBiomes() {
		EndBiomes.addSubBiomeIntegration(OLD_BULBIS_GARDENS, Integrations.BYG.getID("bulbis_gardens"));
		EndBiomes.addSubBiomeIntegration(NIGHTSHADE_REDWOODS, Integrations.BYG.getID("nightshade_forest"));
		//EndBiomes.addSubBiomeIntegration(ETHERIAL_GROVE, Integrations.BYG.getID("ethereal_islands"));
	}
}
