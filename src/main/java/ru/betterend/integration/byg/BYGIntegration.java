package ru.betterend.integration.byg;

import net.minecraft.world.level.block.Block;
import ru.bclib.api.TagAPI;
import ru.bclib.integration.ModIntegration;
import ru.betterend.integration.EndBiomeIntegration;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.biomes.BYGBiomes;
import ru.betterend.integration.byg.features.BYGFeatures;

public class BYGIntegration extends ModIntegration implements EndBiomeIntegration {
	public BYGIntegration() {
		super("byg");
	}
	
	@Override
	public void init() {
		/*Block block = Integrations.BYG.getBlock("ivis_phylium");
		if (block != null) {
			TagAPI.addTags(block, TagAPI.BLOCK_END_GROUND, TagAPI.BLOCK_GEN_TERRAIN);
		}
		BYGBlocks.register();
		BYGFeatures.register();
		BYGBiomes.register();*/
	}
	
	@Override
	public void addBiomes() {
		//BYGBiomes.addBiomes();
	}
}
