package ru.betterend.integration.byg;

import ru.betterend.integration.Integrations;
import ru.betterend.integration.ModIntegration;
import ru.betterend.integration.byg.biomes.BYGBiomes;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.TagHelper;

public class BYGIntegration extends ModIntegration {
	public BYGIntegration() {
		super("byg");
	}

	@Override
	public void register() {
		TagHelper.addTags(Integrations.BYG.getBlock("ivis_phylium"), EndTags.END_GROUND, EndTags.GEN_TERRAIN);
		BYGBlocks.register();
		BYGFeatures.register();
		BYGBiomes.register();
	}

	@Override
	public void addBiomes() {
		BYGBiomes.addBiomes();
	}
}
