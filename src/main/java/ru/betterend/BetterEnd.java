package ru.betterend;

import net.fabricmc.api.ModInitializer;
import ru.betterend.config.MainConfig;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.registry.BlockEntityRegistry;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ItemRegistry;
import ru.betterend.util.Logger;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.surface.DoubleBlockSurfaceBuilder;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	public static final MainConfig CONFIG = MainConfig.getInstance();
	
	@Override
	public void onInitialize() {
		DoubleBlockSurfaceBuilder.register();
		ItemRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		FeatureRegistry.register();
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
		CraftingRecipes.register();
		BlockTagRegistry.register();
	}
}
