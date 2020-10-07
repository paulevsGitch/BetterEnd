package ru.betterend.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.ItemRegistry;

public class CraftingRecipes {
	
	public static void register() {
		RecipeBuilder.make("ender_pearl_to_block", BlockRegistry.ENDER_BLOCK)
			.setShape(new String[] { "OO", "OO" })
			.addMaterial('O', Items.ENDER_PEARL)
			.build();
		RecipeBuilder.make("ender_block_to_pearl", Items.ENDER_PEARL)
			.addMaterial('#', BlockRegistry.ENDER_BLOCK)
			.setOutputCount(4)
			.setList("#")
			.build();
		
		RecipeBuilder.make("end_stone_smelter", BlockRegistry.END_STONE_SMELTER)
			.setShape(new String[] { "###", "V V", "###" })
			.addMaterial('#', Blocks.END_STONE_BRICKS)
			.addMaterial('V', Items.BUCKET)
			.build();
		
		String material = "terminite";
		RecipeBuilder.make(material + "_block", BlockRegistry.TERMINITE_BLOCK)
			.setShape(new String[] { "III", "III", "III" })
			.addMaterial('I', ItemRegistry.TERMINITE_INGOT)
			.build();
		
		registerHelmet(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_HELMET);
		registerChestplate(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_CHESTPLATE);
		registerLeggings(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_LEGGINGS);
		registerBoots(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_BOOTS);
		registerShovel(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_SHOVEL);
		registerSword(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_SWORD);
		registerPickaxe(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_PICKAXE);
		registerAxe(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_AXE);
		registerHoe(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_HOE);
		registerHammer(material, ItemRegistry.TERMINITE_INGOT, ItemRegistry.TERMINITE_HAMMER);
		
		material = "aeternium";
		RecipeBuilder.make(material + "_block", BlockRegistry.AETERNIUM_BLOCK)
			.setShape(new String[] { "III", "III", "III" })
			.addMaterial('I', ItemRegistry.AETERNIUM_INGOT)
			.build();
		
		registerHelmet(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_HELMET);
		registerChestplate(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_CHESTPLATE);
		registerLeggings(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_LEGGINGS);
		registerBoots(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_BOOTS);
		registerShovel(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_SHOVEL);
		registerSword(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_SWORD);
		registerPickaxe(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_PICKAXE);
		registerAxe(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_AXE);
		registerHoe(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_HOE);
		registerHammer(material, ItemRegistry.AETERNIUM_INGOT, ItemRegistry.AETERNIUM_HAMMER);
		
		registerHammer("iron", Items.IRON_INGOT, ItemRegistry.IRON_HAMMER);
		registerHammer("golden", Items.GOLD_INGOT, ItemRegistry.GOLDEN_HAMMER);
		registerHammer("diamond", Items.DIAMOND, ItemRegistry.DIAMOND_HAMMER);
		registerHammer("netherite", Items.NETHERITE_INGOT, ItemRegistry.NETHERITE_HAMMER);
		
		RecipeBuilder.make("blue_vine_seed_dye", Items.BLUE_DYE).setList("#").addMaterial('#', BlockRegistry.BLUE_VINE_SEED).build();
		RecipeBuilder.make("creeping_moss_dye", Items.CYAN_DYE).setList("#").addMaterial('#', BlockRegistry.CREEPING_MOSS).build();
		RecipeBuilder.make("umbrella_moss_dye", Items.YELLOW_DYE).setList("#").addMaterial('#', BlockRegistry.UMBRELLA_MOSS).build();
		RecipeBuilder.make("umbrella_moss_tall_dye", Items.YELLOW_DYE).setList("#").addMaterial('#', BlockRegistry.UMBRELLA_MOSS_TALL).build();
	
		AlloyingRecipes.register();
		SmithingRecipes.register();
	}
	
	private static void registerHelmet(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_helmet", result)
			.setShape(new String[] { "III", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerChestplate(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_chestplate", result)
			.setShape(new String[] { "I I", "III", "III" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerLeggings(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_leggings", result)
			.setShape(new String[] { "III", "I I", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerBoots(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_boots", result)
			.setShape(new String[] { "I I", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerShovel(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_shovel", result)
			.setShape(new String[] { "I", "#", "#" })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerSword(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_sword", result)
			.setShape(new String[] { "I", "I", "#" })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerPickaxe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_pickaxe", result)
			.setShape(new String[] { "III", " # ", " # " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerAxe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_axe", result)
			.setShape(new String[] { "II", "#I", "# " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerHoe(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_hoe", result)
			.setShape(new String[] { "II", "# ", "# " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerHammer(String name, Item material, Item result) {
		RecipeBuilder.make(name + "_hammer", result)
			.setShape(new String[] { "I I", "I#I", " # " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
}
