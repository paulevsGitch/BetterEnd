package ru.betterend;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import ru.betterend.api.BetterEndPlugin;
import ru.betterend.config.Configs;
import ru.betterend.effects.EndEnchantments;
import ru.betterend.effects.EndPotions;
import ru.betterend.integration.Integrations;
import ru.betterend.item.GuideBook;
import ru.betterend.recipe.AlloyingRecipes;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.recipe.FurnaceRecipes;
import ru.betterend.recipe.InfusionRecipes;
import ru.betterend.recipe.SmithingRecipes;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndItems;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BonemealUtil;
import ru.betterend.util.Logger;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.generator.TerrainGenerator;
import ru.betterend.world.surface.SurfaceBuilders;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	@Override
	public void onInitialize() {
		EndSounds.register();
		EndItems.register();
		EndBlocks.register();
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
		SmithingRecipes.register();
		InfusionRecipes.register();
		EndStructures.register();
		Integrations.register();
		BonemealUtil.init();
		TerrainGenerator.init();
		
		if (hasGuideBook()) {
			GuideBook.register();
		}
		
		FabricLoader.getInstance().getEntrypoints("betterend", BetterEndPlugin.class).forEach(BetterEndPlugin::register);
		Configs.saveConfigs();
	}
	
	public static boolean hasGuideBook() {
		return FabricLoader.getInstance().isModLoaded("patchouli");
	}
	
	public static Identifier makeID(String path) {
		return new Identifier(MOD_ID, path);
	}
	
	public static String getStringId(String id) {
		return String.format("%s:%s", MOD_ID, id);
	}
	
	public static boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
	
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
}
