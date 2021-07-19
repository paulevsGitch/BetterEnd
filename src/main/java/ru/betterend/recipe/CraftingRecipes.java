package ru.betterend.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.api.TagAPI;
import ru.bclib.recipes.GridRecipe;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class CraftingRecipes {
	
	public static void register() {
		GridRecipe.make(BetterEnd.MOD_ID, "ender_perl_to_block", EndBlocks.ENDER_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("OO", "OO")
				  .addMaterial('O', Items.ENDER_PEARL)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "ender_block_to_perl", Items.ENDER_PEARL)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .addMaterial('#', EndBlocks.ENDER_BLOCK)
				  .setOutputCount(4)
				  .setList("#")
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "end_stone_smelter", EndBlocks.END_STONE_SMELTER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("T#T", "V V", "T#T")
				  .addMaterial('#', Blocks.END_STONE_BRICKS)
				  .addMaterial('T', EndBlocks.THALLASIUM.ingot)
				  .addMaterial('V', TagAPI.FURNACES)
				  .build();
		
		registerPedestal(
			"andesite_pedestal",
			EndBlocks.ANDESITE_PEDESTAL,
			Blocks.POLISHED_ANDESITE_SLAB,
			Blocks.POLISHED_ANDESITE
		);
		registerPedestal(
			"diorite_pedestal",
			EndBlocks.DIORITE_PEDESTAL,
			Blocks.POLISHED_DIORITE_SLAB,
			Blocks.POLISHED_DIORITE
		);
		registerPedestal(
			"granite_pedestal",
			EndBlocks.GRANITE_PEDESTAL,
			Blocks.POLISHED_GRANITE_SLAB,
			Blocks.POLISHED_GRANITE
		);
		registerPedestal("quartz_pedestal", EndBlocks.QUARTZ_PEDESTAL, Blocks.QUARTZ_SLAB, Blocks.QUARTZ_PILLAR);
		registerPedestal("purpur_pedestal", EndBlocks.PURPUR_PEDESTAL, Blocks.PURPUR_SLAB, Blocks.PURPUR_PILLAR);
		
		GridRecipe.make(BetterEnd.MOD_ID, "infusion_pedestal", EndBlocks.INFUSION_PEDESTAL)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape(" Y ", "O#O", " # ")
				  .addMaterial('O', Items.ENDER_PEARL)
				  .addMaterial('Y', Items.ENDER_EYE)
				  .addMaterial('#', Blocks.OBSIDIAN)
				  .build();
		
		String material = "aeternium";
		GridRecipe.make(BetterEnd.MOD_ID, material + "_block", EndBlocks.AETERNIUM_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("III", "III", "III")
				  .addMaterial('I', EndItems.AETERNIUM_INGOT)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, material + "_block_to_ingot", EndItems.AETERNIUM_INGOT)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .addMaterial('#', EndBlocks.AETERNIUM_BLOCK)
				  .setOutputCount(9)
				  .setList("#")
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "blue_vine_seed_dye", Items.BLUE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.BLUE_VINE_SEED)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "creeping_moss_dye", Items.CYAN_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CREEPING_MOSS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "umbrella_moss_dye", Items.YELLOW_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.UMBRELLA_MOSS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "umbrella_moss_tall_dye", Items.YELLOW_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(2)
				  .setList("#")
				  .addMaterial('#', EndBlocks.UMBRELLA_MOSS_TALL)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "shadow_plant_dye", Items.BLACK_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.SHADOW_PLANT)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "paper", Items.PAPER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###")
				  .addMaterial('#', EndItems.END_LILY_LEAF_DRIED)
				  .setOutputCount(3)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "aurora_block", EndBlocks.AURORA_CRYSTAL)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndItems.CRYSTAL_SHARDS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "lotus_block", EndBlocks.END_LOTUS.log)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndBlocks.END_LOTUS_STEM)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "needlegrass_stick", Items.STICK)
				  .setList("#")
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(2)
				  .addMaterial('#', EndBlocks.NEEDLEGRASS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "shadow_berry_seeds", EndBlocks.SHADOW_BERRY)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .setOutputCount(4)
				  .addMaterial('#', EndItems.SHADOW_BERRY_RAW)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "purple_polypore_dye", Items.PURPLE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.PURPLE_POLYPORE)
				  .build();
		
		registerLantern("end_stone_lantern", EndBlocks.END_STONE_LANTERN, Blocks.END_STONE_BRICK_SLAB);
		registerLantern("andesite_lantern", EndBlocks.ANDESITE_LANTERN, Blocks.ANDESITE_SLAB);
		registerLantern("diorite_lantern", EndBlocks.DIORITE_LANTERN, Blocks.DIORITE_SLAB);
		registerLantern("granite_lantern", EndBlocks.GRANITE_LANTERN, Blocks.GRANITE_SLAB);
		registerLantern("quartz_lantern", EndBlocks.QUARTZ_LANTERN, Blocks.QUARTZ_SLAB);
		registerLantern("purpur_lantern", EndBlocks.PURPUR_LANTERN, Blocks.PURPUR_SLAB);
		registerLantern("blackstone_lantern", EndBlocks.BLACKSTONE_LANTERN, Blocks.BLACKSTONE_SLAB);
		
		GridRecipe.make(BetterEnd.MOD_ID, "amber_gem", EndItems.AMBER_GEM)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndItems.RAW_AMBER)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "amber_block", EndBlocks.AMBER_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndItems.AMBER_GEM)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "amber_gem_block", EndItems.AMBER_GEM)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(4)
				  .setList("#")
				  .addMaterial('#', EndBlocks.AMBER_BLOCK)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "iron_bulb_lantern", EndBlocks.IRON_BULB_LANTERN)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("C", "I", "#")
				  .addMaterial('C', Items.CHAIN)
				  .addMaterial('I', Items.IRON_INGOT)
				  .addMaterial('#', EndItems.GLOWING_BULB)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "twisted_moss_dye", Items.PINK_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.TWISTED_MOSS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "byshy_grass_dye", Items.MAGENTA_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.BUSHY_GRASS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "tail_moss_dye", Items.GRAY_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.TAIL_MOSS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "petal_block", EndBlocks.HYDRALUX_PETAL_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndItems.HYDRALUX_PETAL)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "petal_white_dye", Items.WHITE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndItems.HYDRALUX_PETAL)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "sweet_berry_jelly", EndItems.SWEET_BERRY_JELLY)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("JWSB")
				  .addMaterial('J', EndItems.GELATINE)
				  .addMaterial('W', PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER))
				  .addMaterial('S', Items.SUGAR)
				  .addMaterial('B', Items.SWEET_BERRIES)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "shadow_berry_jelly", EndItems.SHADOW_BERRY_JELLY)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("JWSB")
				  .addMaterial('J', EndItems.GELATINE)
				  .addMaterial('W', PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER))
				  .addMaterial('S', Items.SUGAR)
				  .addMaterial('B', EndItems.SHADOW_BERRY_COOKED)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "shadow_berry_jelly", EndItems.BLOSSOM_BERRY_JELLY)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("JWSB")
				  .addMaterial('J', EndItems.GELATINE)
				  .addMaterial('W', PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER))
				  .addMaterial('S', Items.SUGAR)
				  .addMaterial('B', EndItems.BLOSSOM_BERRY)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "sulphur_gunpowder", Items.GUNPOWDER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("SCB")
				  .addMaterial('S', EndItems.CRYSTALLINE_SULPHUR)
				  .addMaterial('C', Items.COAL, Items.CHARCOAL)
				  .addMaterial('B', Items.BONE_MEAL)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "dense_emerald_ice", EndBlocks.DENSE_EMERALD_ICE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndBlocks.EMERALD_ICE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "ancient_emerald_ice", EndBlocks.ANCIENT_EMERALD_ICE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###", "###", "###")
				  .addMaterial('#', EndBlocks.DENSE_EMERALD_ICE)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_cyan_dye", Items.CYAN_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_CYAN)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_green_dye", Items.GREEN_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_GREEN)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_light_blue_dye", Items.LIGHT_BLUE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_LIGHT_BLUE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_orange_dye", Items.ORANGE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_ORANGE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_purple_dye", Items.PURPLE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_PURPLE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charnia_red_dye", Items.RED_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARNIA_RED)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "respawn_obelisk", EndBlocks.RESPAWN_OBELISK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("CSC", "CSC", "AAA")
				  .addMaterial('C', EndBlocks.AURORA_CRYSTAL)
				  .addMaterial('S', EndItems.ETERNAL_CRYSTAL)
				  .addMaterial('A', EndBlocks.AMBER_BLOCK)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "twisted_umbrella_moss_dye", Items.PURPLE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("#")
				  .addMaterial('#', EndBlocks.TWISTED_UMBRELLA_MOSS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "twisted_umbrella_moss_dye_tall", Items.PURPLE_DYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(2)
				  .setList("#")
				  .addMaterial('#', EndBlocks.TWISTED_UMBRELLA_MOSS_TALL)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "leather_to_stripes", EndItems.LEATHER_STRIPE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("L")
				  .addMaterial('L', Items.LEATHER)
				  .setOutputCount(3)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "stripes_to_leather", Items.LEATHER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("SSS")
				  .addMaterial('S', EndItems.LEATHER_STRIPE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "leather_wrapped_stick", EndItems.LEATHER_WRAPPED_STICK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("SL")
				  .addMaterial('S', Items.STICK)
				  .addMaterial('L', EndItems.LEATHER_STRIPE)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "fiber_string", Items.STRING)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(6)
				  .setShape("#", "#", "#")
				  .addMaterial('#', EndItems.SILK_FIBER)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "ender_eye_amber", Items.ENDER_EYE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("SAS", "APA", "SAS")
				  .addMaterial('S', EndItems.CRYSTAL_SHARDS)
				  .addMaterial('A', EndItems.AMBER_GEM)
				  .addMaterial('P', Items.ENDER_PEARL)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "iron_chandelier", EndBlocks.IRON_CHANDELIER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("I#I", " # ")
				  .addMaterial('#', Items.IRON_INGOT)
				  .addMaterial('I', EndItems.LUMECORN_ROD)
				  .setGroup("end_metal_chandelier")
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "gold_chandelier", EndBlocks.GOLD_CHANDELIER)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("I#I", " # ")
				  .addMaterial('#', Items.GOLD_INGOT)
				  .addMaterial('I', EndItems.LUMECORN_ROD)
				  .setGroup("end_metal_chandelier")
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "missing_tile", EndBlocks.MISSING_TILE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(4)
				  .setShape("#P", "P#")
				  .addMaterial('#', EndBlocks.VIOLECITE.stone, EndBlocks.VIOLECITE.bricks, EndBlocks.VIOLECITE.tiles)
				  .addMaterial('P', Blocks.PURPUR_BLOCK)
				  .build();
		
		registerHammer("iron", Items.IRON_INGOT, EndItems.IRON_HAMMER);
		registerHammer("golden", Items.GOLD_INGOT, EndItems.GOLDEN_HAMMER);
		registerHammer("diamond", Items.DIAMOND, EndItems.DIAMOND_HAMMER);
		
		GridRecipe.make(BetterEnd.MOD_ID, "charcoal_block", EndBlocks.CHARCOAL_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###", "###", "###")
				  .addMaterial('#', Items.CHARCOAL)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "charcoal_from_block", Items.CHARCOAL)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(9)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CHARCOAL_BLOCK)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "end_stone_furnace", EndBlocks.END_STONE_FURNACE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###", "# #", "###")
				  .addMaterial('#', Blocks.END_STONE)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "filalux_lantern", EndBlocks.FILALUX_LANTERN)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###", "###", "###")
				  .addMaterial('#', EndBlocks.FILALUX)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "silk_moth_hive", EndBlocks.SILK_MOTH_HIVE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("#L#", "LML", "#L#")
				  .addMaterial('#', EndBlocks.TENANEA.planks)
				  .addMaterial('L', EndBlocks.TENANEA_LEAVES)
				  .addMaterial('M', EndItems.SILK_MOTH_MATRIX)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "cave_pumpkin_pie", EndItems.CAVE_PUMPKIN_PIE)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("SBS", "BPB", "SBS")
				  .addMaterial('P', EndBlocks.CAVE_PUMPKIN)
				  .addMaterial('B', EndItems.BLOSSOM_BERRY, EndItems.SHADOW_BERRY_RAW)
				  .addMaterial('S', Items.SUGAR)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "cave_pumpkin_seeds", EndBlocks.CAVE_PUMPKIN_SEED)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(4)
				  .setList("#")
				  .addMaterial('#', EndBlocks.CAVE_PUMPKIN)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "neon_cactus_block", EndBlocks.NEON_CACTUS_BLOCK)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("##", "##")
				  .addMaterial('#', EndBlocks.NEON_CACTUS)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "neon_cactus_block_slab", EndBlocks.NEON_CACTUS_BLOCK_SLAB)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("###")
				  .setOutputCount(6)
				  .addMaterial('#', EndBlocks.NEON_CACTUS_BLOCK)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "neon_cactus_block_stairs", EndBlocks.NEON_CACTUS_BLOCK_STAIRS)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("#  ", "## ", "###")
				  .setOutputCount(4)
				  .addMaterial('#', EndBlocks.NEON_CACTUS_BLOCK)
				  .build();
		
		GridRecipe.make(BetterEnd.MOD_ID, "sugar_from_root", Items.SUGAR)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setList("###")
				  .addMaterial('#', EndItems.AMBER_ROOT_RAW)
				  .build();
		GridRecipe.make(BetterEnd.MOD_ID, "endstone_flower_pot", EndBlocks.ENDSTONE_FLOWER_POT)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setOutputCount(3)
				  .setShape("# #", " # ")
				  .addMaterial('#', Blocks.END_STONE_BRICKS)
				  .setGroup("end_pots")
				  .build();
	}
	
	private static void registerLantern(String name, Block lantern, Block slab) {
		GridRecipe.make(BetterEnd.MOD_ID, name, lantern)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("S", "#", "S")
				  .addMaterial('#', EndItems.CRYSTAL_SHARDS)
				  .addMaterial('S', slab)
				  .setGroup("end_stone_lanterns")
				  .build();
	}
	
	public static void registerPedestal(String name, Block pedestal, Block slab, Block pillar) {
		GridRecipe.make(BetterEnd.MOD_ID, name, pedestal)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("S", "#", "S")
				  .addMaterial('S', slab)
				  .addMaterial('#', pillar)
				  .setOutputCount(2)
				  .build();
	}
	
	private static void registerHammer(String name, Item material, Item result) {
		GridRecipe.make(BetterEnd.MOD_ID, name + "_hammer", result)
				  .checkConfig(Configs.RECIPE_CONFIG)
				  .setShape("I I", "I#I", " # ")
				  .addMaterial('I', material)
				  .addMaterial('#', Items.STICK)
				  .build();
	}
}
