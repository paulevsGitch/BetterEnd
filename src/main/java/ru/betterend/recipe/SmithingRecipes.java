package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.recipe.builders.SmithingTableRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class SmithingRecipes {
	
	public static void register() {
		SmithingTableRecipe.create("aeternium_sword_handle")
			.setResult(EndItems.AETERNIUM_SWORD_HANDLE)
			.setBase(EndBlocks.TERMINITE.ingot)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();
		
		SmithingTableRecipe.create("aeternium_sword")
			.setResult(EndItems.AETERNIUM_SWORD)
			.setBase(EndItems.AETERNIUM_SWORD_BLADE)
			.setAddition(EndItems.AETERNIUM_SWORD_HANDLE)
			.build();
		SmithingTableRecipe.create("aeternium_pickaxe")
			.setResult(EndItems.AETERNIUM_PICKAXE)
			.setBase(EndItems.AETERNIUM_PICKAXE_HEAD)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();
		SmithingTableRecipe.create("aeternium_axe")
			.setResult(EndItems.AETERNIUM_AXE)
			.setBase(EndItems.AETERNIUM_AXE_HEAD)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();
		SmithingTableRecipe.create("aeternium_shovel")
			.setResult(EndItems.AETERNIUM_SHOVEL)
			.setBase(EndItems.AETERNIUM_SHOVEL_HEAD)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();
		SmithingTableRecipe.create("aeternium_hoe")
			.setResult(EndItems.AETERNIUM_HOE)
			.setBase(EndItems.AETERNIUM_HOE_HEAD)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();
		SmithingTableRecipe.create("aeternium_hammer")
			.setResult(EndItems.AETERNIUM_HAMMER)
			.setBase(EndItems.AETERNIUM_HAMMER_HEAD)
			.setAddition(EndItems.LEATHER_WRAPPED_STICK)
			.build();

		SmithingTableRecipe.create("netherite_hammer")
			.setResult(EndItems.NETHERITE_HAMMER)
			.setBase(EndItems.DIAMOND_HAMMER)
			.setAddition(Items.NETHERITE_INGOT)
			.build();
		
		SmithingTableRecipe.create("aeternium_helmet")
			.setResult(EndItems.AETERNIUM_HELMET)
			.setBase(EndBlocks.TERMINITE.helmet)
			.setAddition(EndItems.AETERNIUM_INGOT)
			.build();
		SmithingTableRecipe.create("aeternium_chestplate")
			.setResult(EndItems.AETERNIUM_CHESTPLATE)
			.setBase(EndBlocks.TERMINITE.chestplate)
			.setAddition(EndItems.AETERNIUM_INGOT)
			.build();
		SmithingTableRecipe.create("aeternium_leggings")
			.setResult(EndItems.AETERNIUM_LEGGINGS)
			.setBase(EndBlocks.TERMINITE.leggings)
			.setAddition(EndItems.AETERNIUM_INGOT)
			.build();
		SmithingTableRecipe.create("aeternium_boots")
			.setResult(EndItems.AETERNIUM_BOOTS)
			.setBase(EndBlocks.TERMINITE.boots)
			.setAddition(EndItems.AETERNIUM_INGOT)
			.build();
	}
}
