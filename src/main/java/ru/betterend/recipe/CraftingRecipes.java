package ru.betterend.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import ru.betterend.BetterEnd;
import ru.betterend.item.GuideBookItem;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.registry.EndTags;

public class CraftingRecipes {
	
	public static void register() {
		if (BetterEnd.hasGuideBook()) {
			GridRecipe.make("guide_book", GuideBookItem.GUIDE_BOOK)
				.setShape("D", "B", "C")
				.addMaterial('D', EndItems.ENDER_DUST)
				.addMaterial('B', Items.BOOK)
				.addMaterial('C', EndItems.CRYSTAL_SHARDS)
				.build();
		}
		
		GridRecipe.make("ender_pearl_to_block", EndBlocks.ENDER_BLOCK)
			.setShape("OO", "OO")
			.addMaterial('O', Items.ENDER_PEARL)
			.build();
		GridRecipe.make("ender_block_to_pearl", Items.ENDER_PEARL)
			.addMaterial('#', EndBlocks.ENDER_BLOCK)
			.setOutputCount(4)
			.setList("#")
			.build();
		
		GridRecipe.make("end_stone_smelter", EndBlocks.END_STONE_SMELTER)
			.setShape("###", "V V", "###")
			.addMaterial('#', Blocks.END_STONE_BRICKS)
			.addMaterial('V', Items.BUCKET)
			.build();
		
		registerPedestal("andesite_pedestal", EndBlocks.ANDESITE_PEDESTAL, Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_ANDESITE);
		registerPedestal("diorite_pedestal", EndBlocks.DIORITE_PEDESTAL, Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_DIORITE);
		registerPedestal("granite_pedestal", EndBlocks.GRANITE_PEDESTAL, Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_GRANITE);
		registerPedestal("quartz_pedestal", EndBlocks.QUARTZ_PEDESTAL, Blocks.QUARTZ_SLAB, Blocks.QUARTZ_PILLAR);
		registerPedestal("purpur_pedestal", EndBlocks.PURPUR_PEDESTAL, Blocks.PURPUR_SLAB, Blocks.PURPUR_PILLAR);
		
		GridRecipe.make("infusion_pedestal", EndBlocks.INFUSION_PEDESTAL)
			.setShape(" Y ", "O#O", " # ")
			.addMaterial('O', Items.ENDER_PEARL)
			.addMaterial('Y', Items.ENDER_EYE)
			.addMaterial('#', Blocks.OBSIDIAN)
			.build();
		
		String material = "aeternium";
		GridRecipe.make(material + "_block", EndBlocks.AETERNIUM_BLOCK)
			.setShape("III", "III", "III")
			.addMaterial('I', EndItems.AETERNIUM_INGOT)
			.build();
		GridRecipe.make(material + "_block_to_ingot", EndItems.AETERNIUM_INGOT)
			.addMaterial('#', EndBlocks.AETERNIUM_BLOCK)
			.setOutputCount(9)
			.setList("#")
			.build();
		
		GridRecipe.make("blue_vine_seed_dye", Items.BLUE_DYE).setList("#").addMaterial('#', EndBlocks.BLUE_VINE_SEED).build();
		GridRecipe.make("creeping_moss_dye", Items.CYAN_DYE).setList("#").addMaterial('#', EndBlocks.CREEPING_MOSS).build();
		GridRecipe.make("umbrella_moss_dye", Items.YELLOW_DYE).setList("#").addMaterial('#', EndBlocks.UMBRELLA_MOSS).build();
		GridRecipe.make("umbrella_moss_tall_dye", Items.YELLOW_DYE).setOutputCount(2).setList("#").addMaterial('#', EndBlocks.UMBRELLA_MOSS_TALL).build();
		GridRecipe.make("shadow_plant_dye", Items.BLACK_DYE).setList("#").addMaterial('#', EndBlocks.SHADOW_PLANT).build();
		
		GridRecipe.make("paper", Items.PAPER).setShape("###").addMaterial('#', EndItems.END_LILY_LEAF_DRIED).setOutputCount(3).build();
		
		GridRecipe.make("aurora_block", EndBlocks.AURORA_CRYSTAL).setShape("##", "##").addMaterial('#', EndItems.CRYSTAL_SHARDS).build();
		GridRecipe.make("lotus_block", EndBlocks.END_LOTUS.log).setShape("##", "##").addMaterial('#', EndBlocks.END_LOTUS_STEM).build();
		GridRecipe.make("needlegrass_stick", Items.STICK).setList("#").setOutputCount(2).addMaterial('#', EndBlocks.NEEDLEGRASS).build();
		GridRecipe.make("shadow_berry_seeds", EndBlocks.SHADOW_BERRY).setList("#").setOutputCount(4).addMaterial('#', EndItems.SHADOW_BERRY_RAW).build();
		GridRecipe.make("purple_polypore_dye", Items.PURPLE_DYE).setList("#").addMaterial('#', EndBlocks.PURPLE_POLYPORE).build();

		registerLantern("end_stone_lantern", EndBlocks.END_STONE_LANTERN, Blocks.END_STONE_BRICK_SLAB);
		registerLantern("andesite_lantern", EndBlocks.ANDESITE_LANTERN, Blocks.ANDESITE_SLAB);
		registerLantern("diorite_lantern", EndBlocks.DIORITE_LANTERN, Blocks.DIORITE_SLAB);
		registerLantern("granite_lantern", EndBlocks.GRANITE_LANTERN, Blocks.GRANITE_SLAB);
		registerLantern("quartz_lantern", EndBlocks.QUARTZ_LANTERN, Blocks.QUARTZ_SLAB);
		registerLantern("purpur_lantern", EndBlocks.PURPUR_LANTERN, Blocks.PURPUR_SLAB);
		registerLantern("blackstone_lantern", EndBlocks.BLACKSTONE_LANTERN, Blocks.BLACKSTONE_SLAB);
		
		GridRecipe.make("amber_gem", EndItems.AMBER_GEM).setShape("##", "##").addMaterial('#', EndItems.RAW_AMBER).build();
		GridRecipe.make("amber_block", EndBlocks.AMBER_BLOCK).setShape("###", "###", "###").addMaterial('#', EndItems.AMBER_GEM).build();
		GridRecipe.make("iron_bulb_lantern", EndBlocks.IRON_BULB_LANTERN).setShape("C", "I", "#").addMaterial('C', Items.CHAIN).addMaterial('I', Items.IRON_INGOT).addMaterial('#', EndItems.GLOWING_BULB).build();
		GridRecipe.make("twisted_moss_dye", Items.PINK_DYE).setList("#").addMaterial('#', EndBlocks.TWISTED_MOSS).build();
		GridRecipe.make("byshy_grass_dye", Items.MAGENTA_DYE).setList("#").addMaterial('#', EndBlocks.BUSHY_GRASS).build();
		GridRecipe.make("tail_moss_dye", Items.GRAY_DYE).setList("#").addMaterial('#', EndBlocks.TAIL_MOSS).build();
		GridRecipe.make("petal_block", EndBlocks.HYDRALUX_PETAL_BLOCK).setShape("##", "##").addMaterial('#', EndItems.HYDRALUX_PETAL).build();
		GridRecipe.make("petal_white_dye", Items.WHITE_DYE).setList("#").addMaterial('#', EndItems.HYDRALUX_PETAL).build();
		
		GridRecipe.make("sweet_berry_jelly", EndItems.SWEET_BERRY_JELLY)
			.setList("JWSB")
			.addMaterial('J', EndItems.GELATINE)
			.addMaterial('W', PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER))
			.addMaterial('S', Items.SUGAR).addMaterial('B', Items.SWEET_BERRIES)
			.build();
		
		GridRecipe.make("shadow_berry_jelly", EndItems.SHADOW_BERRY_JELLY)
			.setList("JWSB")
			.addMaterial('J', EndItems.GELATINE)
			.addMaterial('W', PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER))
			.addMaterial('S', Items.SUGAR)
			.addMaterial('B', EndItems.SHADOW_BERRY_COOKED)
			.build();
		
		GridRecipe.make("sulphur_gunpowder", Items.GUNPOWDER).setList("SCB").addMaterial('S', EndItems.CRYSTALLINE_SULPHUR).addMaterial('C', Items.COAL, Items.CHARCOAL).addMaterial('B', Items.BONE_MEAL).build();
		
		GridRecipe.make("dense_emerald_ice", EndBlocks.DENSE_EMERALD_ICE).setShape("##", "##").addMaterial('#', EndBlocks.EMERALD_ICE).build();
		GridRecipe.make("ancient_emerald_ice", EndBlocks.ANCIENT_EMERALD_ICE).setShape("###", "###", "###").addMaterial('#', EndBlocks.DENSE_EMERALD_ICE).build();
		
		GridRecipe.make("charnia_cyan_dye", Items.CYAN_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_CYAN).build();
		GridRecipe.make("charnia_green_dye", Items.GREEN_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_GREEN).build();
		GridRecipe.make("charnia_light_blue_dye", Items.LIGHT_BLUE_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_LIGHT_BLUE).build();
		GridRecipe.make("charnia_orange_dye", Items.ORANGE_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_ORANGE).build();
		GridRecipe.make("charnia_purple_dye", Items.PURPLE_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_PURPLE).build();
		GridRecipe.make("charnia_red_dye", Items.RED_DYE).setList("#").addMaterial('#', EndBlocks.CHARNIA_RED).build();
		
		GridRecipe.make("respawn_obelisk", EndBlocks.RESPAWN_OBELISK)
			.setShape("CSC", "CSC", "AAA")
			.addMaterial('C', EndBlocks.AURORA_CRYSTAL)
			.addMaterial('S', EndItems.ETERNAL_CRYSTAL)
			.addMaterial('A', EndBlocks.AMBER_BLOCK)
			.build();
		
		GridRecipe.make("hopper", Blocks.HOPPER)
			.setShape("I I", "ICI", " I ")
			.addMaterial('I', Items.IRON_INGOT)
			.addMaterial('C', EndTags.ITEM_CHEST)
			.build();
		
		GridRecipe.make("shulker_box", Blocks.SHULKER_BOX)
			.setShape("S", "C", "S")
			.addMaterial('S', Items.SHULKER_SHELL)
			.addMaterial('C', EndTags.ITEM_CHEST)
			.build();
		
		GridRecipe.make("twisted_umbrella_moss_dye", Items.PURPLE_DYE).setList("#").addMaterial('#', EndBlocks.TWISTED_UMBRELLA_MOSS).build();
		GridRecipe.make("twisted_umbrella_moss_dye_tall", Items.PURPLE_DYE).setOutputCount(2).setList("#").addMaterial('#', EndBlocks.TWISTED_UMBRELLA_MOSS_TALL).build();
		
		GridRecipe.make("leather_to_stripes", EndItems.LEATHER_STRIPE)
			.setList("L")
			.addMaterial('L', Items.LEATHER)
			.setOutputCount(3)
			.build();
		GridRecipe.make("stripes_to_leather", Items.LEATHER)
			.setList("SSS")
			.addMaterial('S', EndItems.LEATHER_STRIPE)
			.build();
		GridRecipe.make("leather_wrapped_stick", EndItems.LEATHER_WRAPPED_STICK)
			.setList("SL")
			.addMaterial('S', Items.STICK)
			.addMaterial('L', EndItems.LEATHER_STRIPE)
			.build();
		
		GridRecipe.make("fiber_string", Items.STRING).setOutputCount(6).setShape("#", "#", "#").addMaterial('#', EndItems.SILK_FIBER).build();
		
		GridRecipe.make("ender_eye_amber", Items.ENDER_EYE)
		.setShape("SAS", "APA", "SAS")
		.addMaterial('S', EndItems.CRYSTAL_SHARDS)
		.addMaterial('A', EndItems.AMBER_GEM)
		.addMaterial('P', Items.ENDER_PEARL)
		.build();
		
		GridRecipe.make("iron_chandelier", EndBlocks.IRON_CHANDELIER).setShape("I#I", " # ").addMaterial('#', Items.IRON_INGOT).addMaterial('I', EndItems.LUMECORN_ROD).setGroup("end_metal_chandelier").build();
		GridRecipe.make("gold_chandelier", EndBlocks.GOLD_CHANDELIER).setShape("I#I", " # ").addMaterial('#', Items.GOLD_INGOT).addMaterial('I', EndItems.LUMECORN_ROD).setGroup("end_metal_chandelier").build();
		
		GridRecipe.make("missing_tile", EndBlocks.MISSING_TILE)
		.setOutputCount(4)
		.setShape("#P", "P#")
		.addMaterial('#', EndBlocks.VIOLECITE.stone, EndBlocks.VIOLECITE.bricks, EndBlocks.VIOLECITE.tiles)
		.addMaterial('P', Blocks.PURPUR_BLOCK)
		.build();

		registerHammer("iron", Items.IRON_INGOT, EndItems.IRON_HAMMER);
		registerHammer("golden", Items.GOLD_INGOT, EndItems.GOLDEN_HAMMER);
		registerHammer("diamond", Items.DIAMOND, EndItems.DIAMOND_HAMMER);

		GridRecipe.make("charcoal_block", EndBlocks.CHARCOAL_BLOCK).setShape("###", "###", "###").addMaterial('#', Items.CHARCOAL).build();
		GridRecipe.make("end_stone_furnace", EndBlocks.END_STONE_FURNACE).setShape("###", "# #", "###").addMaterial('#', Blocks.END_STONE).build();
	}
	
	private static void registerLantern(String name, Block lantern, Block slab) {
		GridRecipe.make(name, lantern)
			.setShape("S", "#", "S")
			.addMaterial('#', EndItems.CRYSTAL_SHARDS)
			.addMaterial('S', slab)
			.setGroup("end_stone_lanterns")
			.build();
	}
	
	public static void registerPedestal(String name, Block pedestal, Block slab, Block pillar) {
		GridRecipe.make(name, pedestal)
			.setShape("S", "#", "S")
			.addMaterial('S', slab)
			.addMaterial('#', pillar)
			.setOutputCount(2)
			.build();
	}

	private static void registerHammer(String name, Item material, Item result) {
		GridRecipe.make(name + "_hammer", result)
				.setShape("I I", "I#I", " # ")
				.addMaterial('I', material)
				.addMaterial('#', Items.STICK)
				.build();
	}
}
