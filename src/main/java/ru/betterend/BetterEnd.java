package ru.betterend;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import ru.bclib.api.WorldDataAPI;
import ru.bclib.util.Logger;
import ru.betterend.api.BetterEndPlugin;
import ru.betterend.config.Configs;
import ru.betterend.effects.EndEnchantments;
import ru.betterend.effects.EndPotions;
import ru.betterend.integration.Integrations;
import ru.betterend.recipe.AlloyingRecipes;
import ru.betterend.recipe.AnvilRecipes;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.recipe.FurnaceRecipes;
import ru.betterend.recipe.InfusionRecipes;
import ru.betterend.recipe.SmithingRecipes;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndPortals;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BonemealPlants;
import ru.betterend.util.LootTableUtil;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.generator.GeneratorOptions;
import ru.betterend.world.surface.SurfaceBuilders;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = new Logger(MOD_ID);

	@Override
	public void onInitialize() {
		WorldDataAPI.registerModCache(MOD_ID);
		EndPortals.loadPortals();
		EndSounds.register();
		EndBlockEntities.register();
		EndFeatures.register();
		EndEntities.register();
		SurfaceBuilders.register();
		EndBiomes.register();
		BetterEndBiomeSource.register();
		EndTags.register();
		EndEnchantments.register();
		EndPotions.register();
		CraftingRecipes.register();
		FurnaceRecipes.register();
		AlloyingRecipes.register();
		AnvilRecipes.register();
		SmithingRecipes.register();
		InfusionRecipes.register();
		EndStructures.register();
		BonemealPlants.init();
		GeneratorOptions.init();
		LootTableUtil.init();
		FabricLoader.getInstance().getEntrypoints("betterend", BetterEndPlugin.class).forEach(BetterEndPlugin::register);
		Integrations.init();
		Configs.saveConfigs();
	}

	public static ResourceLocation makeID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static String getStringId(String id) {
		return String.format("%s:%s", MOD_ID, id);
	}

	public static boolean isModId(ResourceLocation id) {
		return id.getNamespace().equals(MOD_ID);
	}
}
