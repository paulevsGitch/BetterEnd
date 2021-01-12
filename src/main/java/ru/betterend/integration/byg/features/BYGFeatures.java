package ru.betterend.integration.byg.features;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.BYGBlocks;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.SinglePlantFeature;
import ru.betterend.world.features.VineFeature;
import ru.betterend.world.features.WallPlantFeature;
import ru.betterend.world.features.WallPlantOnLogFeature;

public class BYGFeatures {
	public static final EndFeature OLD_BULBIS_TREE = new EndFeature("old_bulbis_tree", new OldBulbisTreeFeature(), 1);
	public static final EndFeature IVIS_SPROUT = new EndFeature("ivis_sprout", new SinglePlantFeature(Integrations.BYG.getBlock("ivis_sprout"), 6, 2), 6);
	public static final EndFeature IVIS_VINE = new EndFeature("ivis_vine", new VineFeature(BYGBlocks.IVIS_VINE, 24), 5);
	public static final EndFeature IVIS_MOSS = new EndFeature("ivis_moss", new WallPlantFeature(BYGBlocks.IVIS_MOSS, 6), 1);
	public static final EndFeature IVIS_MOSS_WOOD = new EndFeature("ivis_moss_wood", new WallPlantOnLogFeature(BYGBlocks.IVIS_MOSS, 6), 15);
	public static final EndFeature BULBIS_ODDITY = new EndFeature("bulbis_oddity", new SinglePlantFeature(Integrations.BYG.getBlock("bulbis_oddity"), 4, 4), 5);
	public static final EndFeature PURPLE_BULBIS_ODDITY = new EndFeature("purple_bulbis_oddity", new SinglePlantFeature(Integrations.BYG.getBlock("purple_bulbis_oddity"), 4, 4), 5);
	public static final EndFeature NIGHTSHADE_MOSS = new EndFeature("nightshade_moss", new WallPlantFeature(BYGBlocks.NIGHTSHADE_MOSS, 5), 2);
	public static final EndFeature NIGHTSHADE_MOSS_WOOD = new EndFeature("nightshade_moss_wood", new WallPlantOnLogFeature(BYGBlocks.NIGHTSHADE_MOSS, 5), 8);
	
	public static final EndFeature NIGHTSHADE_REDWOOD_TREE = new EndFeature("nightshade_redwood_tree", new NightshadeRedwoodTreeFeature(), 1);
	public static final EndFeature BIG_ETHER_TREE = new EndFeature("big_ether_tree", new BigEtherTreeFeature(), 1);
	
	public static final ConfiguredFeature<?,?> BULBIS_TREES = Integrations.BYG.getConfiguredFeature("rs_sparse_bulbis_tree");
	public static final ConfiguredFeature<?,?> PURPLE_BULBIS_TREES = Integrations.BYG.getConfiguredFeature("rs_sparse_purple_bulbis_tree");
	
	public static void register() {}
}
