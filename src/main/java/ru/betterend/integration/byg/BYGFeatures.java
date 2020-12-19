package ru.betterend.integration.byg;

import ru.betterend.integration.Integrations;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.SinglePlantFeature;

public class BYGFeatures {
	public static final EndFeature OLD_BULBIS_TREE = new EndFeature("old_bulbis_tree", new OldBulbisTreeFeature(), 1);
	public static final EndFeature IVIS_SPROUT = new EndFeature("ivis_sprout", new SinglePlantFeature(Integrations.BYG.getBlock("ivis_sprout"), 6), 9);
	
	public static void register() {}
}
