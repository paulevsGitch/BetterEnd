package ru.betterend;

import net.fabricmc.api.ModInitializer;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.world.generator.BetterEndBiomeSource;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	
	@Override
	public void onInitialize() {
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
	}
}
