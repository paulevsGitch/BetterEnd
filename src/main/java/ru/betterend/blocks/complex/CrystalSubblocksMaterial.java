package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BaseRotatedPillarBlock;
import ru.bclib.blocks.BaseSlabBlock;
import ru.bclib.blocks.BaseStairsBlock;
import ru.bclib.blocks.BaseWallBlock;
import ru.bclib.recipes.GridRecipe;
import ru.bclib.util.TagHelper;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.EndPedestal;
import ru.betterend.config.Configs;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.registry.EndBlocks;

public class CrystalSubblocksMaterial {
	public final Block polished;
	public final Block tiles;
	public final Block pillar;
	public final Block stairs;
	public final Block slab;
	public final Block wall;
	public final Block pedestal;
	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_wall;

	public CrystalSubblocksMaterial(String name, Block source) {
		FabricBlockSettings material = FabricBlockSettings.copyOf(source);
		polished = EndBlocks.registerBlock(name + "_polished", new BaseBlock(material));
		tiles = EndBlocks.registerBlock(name + "_tiles", new BaseBlock(material));
		pillar = EndBlocks.registerBlock(name + "_pillar", new BaseRotatedPillarBlock(material));
		stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock(source));
		slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock(source));
		wall = EndBlocks.registerBlock(name + "_wall", new BaseWallBlock(source));
		pedestal = EndBlocks.registerBlock(name + "_pedestal", new EndPedestal(source));
		bricks = EndBlocks.registerBlock(name + "_bricks", new BaseBlock(material));
		brick_stairs = EndBlocks.registerBlock(name + "_bricks_stairs", new BaseStairsBlock(bricks));
		brick_slab = EndBlocks.registerBlock(name + "_bricks_slab", new BaseSlabBlock(bricks));
		brick_wall = EndBlocks.registerBlock(name + "_bricks_wall", new BaseWallBlock(bricks));

		// Recipes //
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks", bricks).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', source).setGroup("end_bricks").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_polished", polished).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_tiles", tiles).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_pillar", pillar).checkConfig(Configs.RECIPE_CONFIG).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();

		GridRecipe.make(BetterEnd.MOD_ID, name + "_stairs", stairs).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', source).setGroup("end_stone_stairs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_slab", slab).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###").addMaterial('#', source).setGroup("end_stone_slabs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_stairs", brick_stairs).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_slab", brick_slab).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();

		GridRecipe.make(BetterEnd.MOD_ID, name + "_wall", wall).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###", "###").addMaterial('#', source).setGroup("end_wall").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bricks_wall", brick_wall).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();

		CraftingRecipes.registerPedestal(name + "_pedestal", pedestal, slab, pillar);

		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, source);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, source);

		// Block Tags //
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
	}
}