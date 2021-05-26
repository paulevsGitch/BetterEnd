package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import ru.bclib.util.TagHelper;
import ru.betterend.blocks.EndPedestal;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.blocks.basis.EndPillarBlock;
import ru.betterend.blocks.basis.EndSlabBlock;
import ru.betterend.blocks.basis.EndStairsBlock;
import ru.betterend.blocks.basis.EndWallBlock;
import ru.betterend.recipe.CraftingRecipes;
import ru.betterend.recipe.builders.GridRecipe;
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
		polished = EndBlocks.registerBlock(name + "_polished", new BlockBase(material));
		tiles = EndBlocks.registerBlock(name + "_tiles", new BlockBase(material));
		pillar = EndBlocks.registerBlock(name + "_pillar", new EndPillarBlock(material));
		stairs = EndBlocks.registerBlock(name + "_stairs", new EndStairsBlock(source));
		slab = EndBlocks.registerBlock(name + "_slab", new EndSlabBlock(source));
		wall = EndBlocks.registerBlock(name + "_wall", new EndWallBlock(source));
		pedestal = EndBlocks.registerBlock(name + "_pedestal", new EndPedestal(source));
		bricks = EndBlocks.registerBlock(name + "_bricks", new BlockBase(material));
		brick_stairs = EndBlocks.registerBlock(name + "_bricks_stairs", new EndStairsBlock(bricks));
		brick_slab = EndBlocks.registerBlock(name + "_bricks_slab", new EndSlabBlock(bricks));
		brick_wall = EndBlocks.registerBlock(name + "_bricks_wall", new EndWallBlock(bricks));
		
		// Recipes //
		GridRecipe.make(name + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', source).setGroup("end_bricks").build();
		GridRecipe.make(name + "_polished", polished).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		GridRecipe.make(name + "_tiles", tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
		GridRecipe.make(name + "_pillar", pillar).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
		
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', source).setGroup("end_stone_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', source).setGroup("end_stone_slabs").build();
		GridRecipe.make(name + "_bricks_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		GridRecipe.make(name + "_bricks_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
		
		GridRecipe.make(name + "_wall", wall).setOutputCount(6).setShape("###", "###").addMaterial('#', source).setGroup("end_wall").build();
		GridRecipe.make(name + "_bricks_wall", brick_wall).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
		
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