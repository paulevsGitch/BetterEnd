package ru.betterend.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class CraftingRecipes {
	
	public static void register() {
		GridRecipe.make("ender_pearl_to_block", EndBlocks.ENDER_BLOCK)
			.setShape(new String[] { "OO", "OO" })
			.addMaterial('O', Items.ENDER_PEARL)
			.build();
		GridRecipe.make("ender_block_to_pearl", Items.ENDER_PEARL)
			.addMaterial('#', EndBlocks.ENDER_BLOCK)
			.setOutputCount(4)
			.setList("#")
			.build();
		
		GridRecipe.make("end_stone_smelter", EndBlocks.END_STONE_SMELTER)
			.setShape(new String[] { "###", "V V", "###" })
			.addMaterial('#', Blocks.END_STONE_BRICKS)
			.addMaterial('V', Items.BUCKET)
			.build();
		
		registerPedestal("andesite_pedestal", EndBlocks.ANDESITE_PEDESTAL, Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_ANDESITE);
		registerPedestal("diorite_pedestal", EndBlocks.DIORITE_PEDESTAL, Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_DIORITE);
		registerPedestal("granite_pedestal", EndBlocks.GRANITE_PEDESTAL, Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_GRANITE);
		registerPedestal("quartz_pedestal", EndBlocks.QUARTZ_PEDESTAL, Blocks.QUARTZ_SLAB, Blocks.QUARTZ_PILLAR);
		registerPedestal("purpur_pedestal", EndBlocks.PURPUR_PEDESTAL, Blocks.PURPUR_SLAB, Blocks.PURPUR_PILLAR);
		
		String material = "terminite";
		GridRecipe.make(material + "_block", EndBlocks.TERMINITE_BLOCK)
			.setShape(new String[] { "III", "III", "III" })
			.addMaterial('I', EndItems.TERMINITE_INGOT)
			.build();
		GridRecipe.make(material + "_block_to_ingot", EndItems.TERMINITE_INGOT)
			.addMaterial('#', EndBlocks.TERMINITE_BLOCK)
			.setOutputCount(9)
			.setList("#")
			.build();
		
		registerHelmet(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_HELMET);
		registerChestplate(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_CHESTPLATE);
		registerLeggings(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_LEGGINGS);
		registerBoots(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_BOOTS);
		registerShovel(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_SHOVEL);
		registerSword(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_SWORD);
		registerPickaxe(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_PICKAXE);
		registerAxe(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_AXE);
		registerHoe(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_HOE);
		registerHammer(material, EndItems.TERMINITE_INGOT, EndItems.TERMINITE_HAMMER);
		
		material = "aeternium";
		GridRecipe.make(material + "_block", EndBlocks.AETERNIUM_BLOCK)
			.setShape(new String[] { "III", "III", "III" })
			.addMaterial('I', EndItems.AETERNIUM_INGOT)
			.build();
		GridRecipe.make(material + "_block_to_ingot", EndItems.AETERNIUM_INGOT)
			.addMaterial('#', EndBlocks.AETERNIUM_BLOCK)
			.setOutputCount(9)
			.setList("#")
			.build();
		
		registerHelmet(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_HELMET);
		registerChestplate(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_CHESTPLATE);
		registerLeggings(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_LEGGINGS);
		registerBoots(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_BOOTS);
		registerShovel(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_SHOVEL);
		registerSword(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_SWORD);
		registerPickaxe(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_PICKAXE);
		registerAxe(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_AXE);
		registerHoe(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_HOE);
		registerHammer(material, EndItems.AETERNIUM_INGOT, EndItems.AETERNIUM_HAMMER);
		
		registerHammer("iron", Items.IRON_INGOT, EndItems.IRON_HAMMER);
		registerHammer("golden", Items.GOLD_INGOT, EndItems.GOLDEN_HAMMER);
		registerHammer("diamond", Items.DIAMOND, EndItems.DIAMOND_HAMMER);
		registerHammer("netherite", Items.NETHERITE_INGOT, EndItems.NETHERITE_HAMMER);
		
		GridRecipe.make("blue_vine_seed_dye", Items.BLUE_DYE).setList("#").addMaterial('#', EndBlocks.BLUE_VINE_SEED).build();
		GridRecipe.make("creeping_moss_dye", Items.CYAN_DYE).setList("#").addMaterial('#', EndBlocks.CREEPING_MOSS).build();
		GridRecipe.make("umbrella_moss_dye", Items.YELLOW_DYE).setList("#").addMaterial('#', EndBlocks.UMBRELLA_MOSS).build();
		GridRecipe.make("umbrella_moss_tall_dye", Items.YELLOW_DYE).setList("#").addMaterial('#', EndBlocks.UMBRELLA_MOSS_TALL).build();
		GridRecipe.make("shadow_plant_dye", Items.BLACK_DYE).setList("#").addMaterial('#', EndBlocks.SHADOW_PLANT).build();
		
		GridRecipe.make("paper", Items.PAPER).setShape("###").addMaterial('#', EndItems.END_LILY_LEAF_DRIED).setOutputCount(3).build();
		
		GridRecipe.make("aurora_block", EndBlocks.AURORA_CRYSTAL).setShape("##", "##").addMaterial('#', EndItems.CRYSTAL_SHARDS).build();
		GridRecipe.make("lotus_block", EndBlocks.END_LOTUS.log).setShape("##", "##").addMaterial('#', EndBlocks.END_LOTUS_STEM).build();
		GridRecipe.make("needlegrass_stick", Items.STICK).setList("#").setOutputCount(2).addMaterial('#', EndBlocks.NEEDLEGRASS).build();
	}
	
	public static void registerPedestal(String name, Block pedestal, Block slab, Block pillar) {
		GridRecipe.make(name, pedestal)
			.setShape(new String[] { "S", "#", "S" })
			.addMaterial('S', slab)
			.addMaterial('#', pillar)
			.setOutputCount(2)
			.build();
	}
	
	private static void registerHelmet(String name, Item material, Item result) {
		GridRecipe.make(name + "_helmet", result)
			.setShape(new String[] { "III", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerChestplate(String name, Item material, Item result) {
		GridRecipe.make(name + "_chestplate", result)
			.setShape(new String[] { "I I", "III", "III" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerLeggings(String name, Item material, Item result) {
		GridRecipe.make(name + "_leggings", result)
			.setShape(new String[] { "III", "I I", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerBoots(String name, Item material, Item result) {
		GridRecipe.make(name + "_boots", result)
			.setShape(new String[] { "I I", "I I" })
			.addMaterial('I', material)
			.build();
	}
	
	private static void registerShovel(String name, Item material, Item result) {
		GridRecipe.make(name + "_shovel", result)
			.setShape(new String[] { "I", "#", "#" })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerSword(String name, Item material, Item result) {
		GridRecipe.make(name + "_sword", result)
			.setShape(new String[] { "I", "I", "#" })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerPickaxe(String name, Item material, Item result) {
		GridRecipe.make(name + "_pickaxe", result)
			.setShape(new String[] { "III", " # ", " # " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerAxe(String name, Item material, Item result) {
		GridRecipe.make(name + "_axe", result)
			.setShape(new String[] { "II", "#I", "# " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerHoe(String name, Item material, Item result) {
		GridRecipe.make(name + "_hoe", result)
			.setShape(new String[] { "II", "# ", "# " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
	
	private static void registerHammer(String name, Item material, Item result) {
		GridRecipe.make(name + "_hammer", result)
			.setShape(new String[] { "I I", "I#I", " # " })
			.addMaterial('I', material)
			.addMaterial('#', Items.STICK)
			.build();
	}
}
