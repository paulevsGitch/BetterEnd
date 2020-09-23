package ru.betterend;

import net.fabricmc.api.ModInitializer;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.util.Logger;
import ru.betterend.world.generator.BetterEndBiomeSource;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	
	@Override
	public void onInitialize() {
		FeatureRegistry.register();
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
	}
}
