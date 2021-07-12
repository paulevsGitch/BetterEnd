package ru.betterend.recipe;

import net.minecraft.world.item.Items;
import ru.bclib.recipes.SmithingTableRecipe;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class SmithingRecipes {
	
	public static void register() {
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_sword_handle").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_SWORD_HANDLE).setBase(EndBlocks.TERMINITE.ingot).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_sword").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_SWORD).setBase(EndItems.AETERNIUM_SWORD_BLADE).setAddition(EndItems.AETERNIUM_SWORD_HANDLE).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_pickaxe").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_PICKAXE).setBase(EndItems.AETERNIUM_PICKAXE_HEAD).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_axe").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_AXE).setBase(EndItems.AETERNIUM_AXE_HEAD).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_shovel").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_SHOVEL).setBase(EndItems.AETERNIUM_SHOVEL_HEAD).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_hoe").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_HOE).setBase(EndItems.AETERNIUM_HOE_HEAD).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_hammer").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_HAMMER).setBase(EndItems.AETERNIUM_HAMMER_HEAD).setAddition(EndItems.LEATHER_WRAPPED_STICK).build();
		
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "netherite_hammer").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.NETHERITE_HAMMER).setBase(EndItems.DIAMOND_HAMMER).setAddition(Items.NETHERITE_INGOT).build();
		
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_helmet").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_HELMET).setBase(EndBlocks.TERMINITE.helmet).setAddition(EndItems.AETERNIUM_FORGED_PLATE).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_chestplate").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_CHESTPLATE).setBase(EndBlocks.TERMINITE.chestplate).setAddition(EndItems.AETERNIUM_FORGED_PLATE).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_leggings").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_LEGGINGS).setBase(EndBlocks.TERMINITE.leggings).setAddition(EndItems.AETERNIUM_FORGED_PLATE).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "aeternium_boots").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.AETERNIUM_BOOTS).setBase(EndBlocks.TERMINITE.boots).setAddition(EndItems.AETERNIUM_FORGED_PLATE).build();
		
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "thallasium_anvil_updrade").checkConfig(Configs.RECIPE_CONFIG).setResult(EndBlocks.TERMINITE.anvilBlock).setBase(EndBlocks.THALLASIUM.anvilBlock).setAddition(EndBlocks.TERMINITE.block).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "terminite_anvil_updrade").checkConfig(Configs.RECIPE_CONFIG).setResult(EndBlocks.AETERNIUM_ANVIL).setBase(EndBlocks.TERMINITE.anvilBlock).setAddition(EndItems.AETERNIUM_INGOT).build();
		
		SmithingTableRecipe.create(BetterEnd.MOD_ID, "armored_elytra").checkConfig(Configs.RECIPE_CONFIG).setResult(EndItems.ARMORED_ELYTRA).setBase(Items.ELYTRA).setAddition(EndItems.AETERNIUM_INGOT).build();
	}
}
