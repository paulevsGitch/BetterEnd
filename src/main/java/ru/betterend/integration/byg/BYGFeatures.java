package ru.betterend.integration.byg;

import ru.betterend.integration.Integrations;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.VineFeature;
import ru.betterend.world.features.WallPlantFeature;
import ru.betterend.world.features.WallPlantOnLogFeature;

public class BYGFeatures {
	public static final EndFeature OLD_BULBIS_TREE = new EndFeature("old_bulbis_tree", new OldBulbisTreeFeature(), 1);
	public static final EndFeature IVIS_SPROUT = new EndFeature("ivis_sprout", new SinglePlantFeature(Integrations.BYG.getBlock("ivis_sprout"), 6), 9);
	public static final EndFeature IVIS_VINE = new EndFeature("ivis_vine", new VineFeature(BYGBlocks.IVIS_VINE, 24), 5);
	public static final EndFeature IVIS_MOSS = new EndFeature("ivis_moss", new WallPlantFeature(BYGBlocks.IVIS_MOSS, 6), 1);
	public static final EndFeature IVIS_MOSS_WOOD = new EndFeature("ivis_moss_wood", new WallPlantOnLogFeature(BYGBlocks.IVIS_MOSS, 6), 15);
	
	public static void register() {}
}
