package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BaseFurnaceBlock;
import ru.bclib.blocks.BaseRotatedPillarBlock;
import ru.bclib.blocks.BaseSlabBlock;
import ru.bclib.blocks.BaseStairsBlock;
import ru.bclib.blocks.BaseStoneButtonBlock;
import ru.bclib.blocks.BaseWallBlock;
import ru.bclib.blocks.StonePressurePlateBlock;
import ru.bclib.recipes.GridRecipe;
import ru.bclib.util.TagHelper;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndPedestal;
import ru.betterend.blocks.FlowerPotBlock;
import ru.betterend.blocks.basis.StoneLanternBlock;
import ru.betterend.config.Configs;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class StoneMaterial {
	public final Block stone;
	
	public final Block polished;
	public final Block tiles;
	public final Block pillar;
	public final Block stairs;
	public final Block slab;
	public final Block wall;
	public final Block button;
	public final Block pressure_plate;
	public final Block pedestal;
	public final Block lantern;
	
	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_wall;
	public final Block furnace;
	public final Block flowerPot;
	
	public StoneMaterial(String name, MaterialColor color) {
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color);
		
		stone = EndBlocks.registerBlock(name, new BaseBlock(material));
		polished = EndBlocks.registerBlock(name + "_polished", new BaseBlock(material));
		tiles = EndBlocks.registerBlock(name + "_tiles", new BaseBlock(material));
		pillar = EndBlocks.registerBlock(name + "_pillar", new BaseRotatedPillarBlock(material));
		stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock(stone));
		slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock(stone));
		wall = EndBlocks.registerBlock(name + "_wall", new BaseWallBlock(stone));
		button = EndBlocks.registerBlock(name + "_button", new BaseStoneButtonBlock(stone));
		pressure_plate = EndBlocks.registerBlock(name + "_plate", new StonePressurePlateBlock(stone));
		pedestal = EndBlocks.registerBlock(name + "_pedestal", new EndPedestal(stone));
		lantern = EndBlocks.registerBlock(name + "_lantern", new StoneLanternBlock(stone));
		
		bricks = EndBlocks.registerBlock(name + "_bricks", new BaseBlock(material));
		brick_stairs = EndBlocks.registerBlock(name + "_bricks_stairs", new BaseStairsBlock(bricks));
		brick_slab = EndBlocks.registerBlock(name + "_bricks_slab", new BaseSlabBlock(bricks));
		brick_wall = EndBlocks.registerBlock(name + "_bricks_wall", new BaseWallBlock(bricks));
		furnace = EndBlocks.registerBlock(name + "_furnace", new BaseFurnaceBlock(bricks));
		flowerPot = EndBlocks.registerBlock(name + "_flower_pot", new FlowerPotBlock(bricks));
		
		// Recipes //
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks", bricks).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("end_bricks").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_polished", polished).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_tiles", tiles).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_pillar", pillar).checkConfig(Configs.RECIPE_CONFIG).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
		
		GridRecipe.make(BetterEnd.MOD_ID, name + "_stairs", stairs).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', stone).setGroup("end_stone_stairs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_slab", slab).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###").addMaterial('#', stone).setGroup("end_stone_slabs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_stairs", brick_stairs).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_slab", brick_slab).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
		
		GridRecipe.make(BetterEnd.MOD_ID, name + "_wall", wall).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###", "###").addMaterial('#', stone).setGroup("end_wall").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_wall", brick_wall).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
		
		GridRecipe.make(BetterEnd.MOD_ID, name + "_button", button).checkConfig(Configs.RECIPE_CONFIG).setList("#").addMaterial('#', stone).setGroup("end_stone_buttons").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_pressure_plate", pressure_plate).checkConfig(Configs.RECIPE_CONFIG).setShape("##").addMaterial('#', stone).setGroup("end_stone_plates").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_lantern", lantern).checkConfig(Configs.RECIPE_CONFIG).setShape("S", "#", "S").addMaterial('#', EndItems.CRYSTAL_SHARDS).addMaterial('S', slab, brick_slab).setGroup("end_stone_lanterns").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_furnace", furnace).checkConfig(Configs.RECIPE_CONFIG).setShape("###", "# #", "###").addMaterial('#', stone).setGroup("end_stone_furnaces").build();
		
		CraftingRecipes.registerPedestal(name + "_pedestal", pedestal, slab, pillar);
		
		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);
		TagHelper.addTag(TagAPI.FURNACES, furnace);
		
		// Block Tags //
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
		TagHelper.addTags(pressure_plate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
		TagHelper.addTag(TagAPI.END_STONES, stone);
		
		TagHelper.addTag(TagAPI.DRAGON_IMMUNE, stone, stairs, slab, wall);
	}
}