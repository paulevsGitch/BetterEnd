package ru.betterend.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.ItemRegistry;

public class CraftingRecipes {
	private static String[] helmet_recipe = new String[] {
		"III", "I I"
	};
	private static String[] chestplate_recipe = new String[] {
		"I I", "III", "III"
	};
	private static String[] leggings_recipe = new String[] {
		"III", "I I", "I I"
	};
	private static String[] boots_recipe = new String[] {
		"I I", "I I"
	};
	private static String[] shovel_recipe = new String[] {
		"I", "#", "#"
	};
	private static String[] sword_recipe = new String[] {
		"I", "I", "#"
	};
	private static String[] pickaxe_recipe = new String[] {
		"III", " # ", " # "
	};
	private static String[] axe_recipe = new String[] {
		"II", "#I", "# "
	};
	private static String[] hoe_recipe = new String[] {
		"II", "# ", "# "
	};
	
	public static void register() {
		if (blockExists(BlockRegistry.ENDER_BLOCK)) {
			RecipeBuilder.make("ender_pearl_to_block", BlockRegistry.ENDER_BLOCK)
				.setShape(new String[] { "OO", "OO" })
				.addMaterial('O', Items.ENDER_PEARL)
				.build();
			RecipeBuilder.make("ender_block_to_pearl", Items.ENDER_PEARL)
				.addMaterial('#', BlockRegistry.ENDER_BLOCK)
				.setOutputCount(4)
				.setList("#")
				.build();
		}
		if (blockExists(BlockRegistry.END_STONE_SMELTER)) {
			RecipeBuilder.make("end_stone_smelter", BlockRegistry.END_STONE_SMELTER)
				.setShape(new String[] { "###", "V#V", "###" })
				.addMaterial('#', Blocks.END_STONE_BRICKS)
				.addMaterial('V', Items.BUCKET)
				.build();
				
		}
		if (itemExists(ItemRegistry.TERMINITE_INGOT)) {
			registerHelmet("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_HELMET);
			registerChestplate("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_CHESTPLATE);
			registerLeggings("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_LEGGINGS);
			registerBoots("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_BOOTS);
			registerShovel("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_SHOVEL);
			registerSword("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_SWORD);
			registerPickaxe("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_PICKAXE);
			registerAxe("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_AXE);
			registerHoe("terminite", ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_HOE);
		}
		if (itemExists(ItemRegistry.AETERNIUM_INGOT)) {
			registerHelmet("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_HELMET);
			registerChestplate("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_CHESTPLATE);
			registerLeggings("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_LEGGINGS);
			registerBoots("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_BOOTS);
			registerShovel("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_SHOVEL);
			registerSword("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_SWORD);
			registerPickaxe("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_PICKAXE);
			registerAxe("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_AXE);
			registerHoe("aeternium", ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_HOE);
		}
	}
	
	private static void registerHelmet(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_helmet", result)
			.setShape(helmet_recipe)
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerChestplate(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_chestplate", result)
			.setShape(chestplate_recipe)
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerLeggings(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_leggings", result)
			.setShape(leggings_recipe)
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerBoots(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_boots", result)
			.setShape(boots_recipe)
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerShovel(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_shovel", result)
			.setShape(shovel_recipe)
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerSword(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_sword", result)
			.setShape(sword_recipe)
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerPickaxe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_pickaxe", result)
			.setShape(pickaxe_recipe)
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerAxe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_axe", result)
			.setShape(axe_recipe)
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerHoe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_hoe", result)
			.setShape(hoe_recipe)
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	protected static boolean itemExists(Item item) {
		return Registry.ITEM.getId(item) != Registry.ITEM.getDefaultId();
	}

	protected static boolean blockExists(Block block) {
		return Registry.BLOCK.getId(block) != Registry.BLOCK.getDefaultId();
	}
}
