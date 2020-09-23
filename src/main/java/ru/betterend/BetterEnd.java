package ru.betterend;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import ru.betterend.config.MainConfig;
import ru.betterend.recipe.RecipeBuilder;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ItemRegistry;
import ru.betterend.util.Logger;
import ru.betterend.world.generator.BetterEndBiomeSource;

public class BetterEnd implements ModInitializer {
	public static final String MOD_ID = "betterend";
	public static final Logger LOGGER = Logger.get();
	public static final MainConfig CONFIG = MainConfig.getInstance();
	
	@Override
	public void onInitialize() {
		ItemRegistry.register();
		BlockRegistry.register();
		FeatureRegistry.register();
		BiomeRegistry.register();
		BetterEndBiomeSource.register();
		
		// TEST //
		new RecipeBuilder("test_block", Blocks.ANVIL)
		.setShape(new String[] {"I#", "#I"})
		.addMaterial('I', Items.STRING)
		.addMaterial('#', Items.APPLE)
		.build();
		
		new RecipeBuilder("test_block_shaped", Blocks.STONE)
		.setShape(new String[] {"I#", "#I"})
		.addMaterial('I', Items.STRING)
		.addMaterial('#', ItemTags.LOGS)
		.build();
		
		new RecipeBuilder("test_item_shapeless", Items.SUGAR)
		.setList("I#Y")
		.addMaterial('I', Items.STRING)
		.addMaterial('#', ItemTags.LOGS)
		.addMaterial('Y', ItemTags.ARROWS)
		.build();
	}
}
