package ru.betterend;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import ru.betterend.api.BetterEndPlugin;
import ru.betterend.config.MainConfig;
import ru.betterend.effects.EndEnchantments;
import ru.betterend.effects.EndPotions;
import ru.betterend.recipe.AlloyingRecipes;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.recipe.SmeltigRecipes;
import ru.betterend.recipe.SmithingRecipes;
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

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	public static final MainConfig CONFIG = MainConfig.getInstance();
	
	@Override
	public void onInitialize() {
		SoundRegistry.register();
		ItemRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		FeatureRegistry.register();
		EntityRegistry.register();
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
		ItemTagRegistry.register();
		BlockTagRegistry.register();
		EndEnchantments.register();
		EndPotions.register();
		CraftingRecipes.register();
		SmeltigRecipes.register();
		AlloyingRecipes.register();
		SmithingRecipes.register();
		StructureRegistry.register();
		
		FabricLoader.getInstance().getEntrypoints("betterend", BetterEndPlugin.class).forEach(BetterEndPlugin::register);
		
		if (isDevEnvironment()) {
			printMissingNames();
		}
	}
	
	public static Identifier makeID(String path) {
		return new Identifier(MOD_ID, path);
	}
	
	public static String getStringId(String id) {
		return String.format("%s:%s", MOD_ID, id);
	}
	
	private static boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
	
	private static void printMissingNames() {
		List<String> missingNames = Lists.newArrayList();
		
		ItemRegistry.getModBlocks().forEach((block) -> {
			String name = block.getName().asString();
			if (name.contains(".betterend.")) {
				missingNames.add(name);
			}
		});
		
		ItemRegistry.getModItems().forEach((item) -> {
			String name = item.getName().asString();
			if (name.contains(".betterend.")) {
				missingNames.add(name);
			}
		});
		
		if (!missingNames.isEmpty()) {
			System.out.println("========================================");
			System.out.println("           MISSING NAMES LIST           ");
			System.out.println("========================================");
			missingNames.forEach((name) -> {
				System.out.println(name);
			});
			System.out.println("========================================");
		}
	}
}
