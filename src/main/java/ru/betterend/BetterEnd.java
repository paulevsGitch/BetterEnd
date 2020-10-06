package ru.betterend;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import ru.betterend.config.MainConfig;
import ru.betterend.recipe.AlloyingRecipes;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.registry.BlockEntityRegistry;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.registry.EntityRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ItemRegistry;
import ru.betterend.registry.ItemTagRegistry;
import ru.betterend.registry.SoundRegistry;
import ru.betterend.registry.StructureRegistry;
import ru.betterend.util.Logger;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.surface.DoubleBlockSurfaceBuilder;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	public static final MainConfig CONFIG = MainConfig.getInstance();
	
	@Override
	public void onInitialize() {
		SoundRegistry.register();
		DoubleBlockSurfaceBuilder.register();
		ItemRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		FeatureRegistry.register();
		EntityRegistry.register();
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
		ItemTagRegistry.register();
		BlockTagRegistry.register();
		CraftingRecipes.register();
		AlloyingRecipes.register();
		StructureRegistry.register();
	}
	
	public static Identifier makeID(String path) {
		return new Identifier(MOD_ID, path);
	}
	
	// For what does this exists? //
	public static String getStringId(String id) {
		return String.format("%s:%s", MOD_ID, id);
	}
}
