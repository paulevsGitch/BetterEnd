package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.blocks.basis.BlockPillar;
import ru.betterend.blocks.basis.BlockSlab;
import ru.betterend.blocks.basis.BlockStairs;
import ru.betterend.blocks.basis.BlockStoneButton;
import ru.betterend.blocks.basis.BlockStonePressurePlate;
import ru.betterend.blocks.basis.BlockWall;
import ru.betterend.recipe.builders.RecipeBuilder;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.TagHelper;

public class StoneMaterial {
	public final Block stone;
	
	public final Block tile;
	public final Block small_tiles;
	public final Block pillar;
	public final Block stairs;
	public final Block slab;
	public final Block wall;
	public final Block button;
	public final Block pressure_plate;
	
	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_wall;
	
	public StoneMaterial(String name, MaterialColor color) {
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color);
		
		stone = BlockRegistry.registerBlock(name, new BlockBase(material));
		tile = BlockRegistry.registerBlock(name + "_tile", new BlockBase(material));
		small_tiles = BlockRegistry.registerBlock(name + "_small_tiles", new BlockBase(material));
		pillar = BlockRegistry.registerBlock(name + "_pillar", new BlockPillar(material));
		stairs = BlockRegistry.registerBlock(name + "_stairs", new BlockStairs(stone));
		slab = BlockRegistry.registerBlock(name + "_slab", new BlockSlab(stone));
		wall = BlockRegistry.registerBlock(name + "_wall", new BlockWall(stone));
		button = BlockRegistry.registerBlock(name + "_button", new BlockStoneButton(stone));
		pressure_plate = BlockRegistry.registerBlock(name + "_plate", new BlockStonePressurePlate(stone));
		
		bricks = BlockRegistry.registerBlock(name + "_bricks", new BlockBase(material));
		brick_stairs = BlockRegistry.registerBlock(name + "_brick_stairs", new BlockStairs(bricks));
		brick_slab = BlockRegistry.registerBlock(name + "_brick_slab", new BlockSlab(bricks));
		brick_wall = BlockRegistry.registerBlock(name + "_brick_wall", new BlockWall(bricks));
		
		// Recipes //
		RecipeBuilder.make(name + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("end_bricks").build();
		RecipeBuilder.make(name + "_tile", tile).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
		RecipeBuilder.make(name + "_small_tiles", small_tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', tile).setGroup("end_small_tile").build();
		RecipeBuilder.make(name + "_pillar", pillar).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
		
		RecipeBuilder.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', stone).setGroup("end_stone_stairs").build();
		RecipeBuilder.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', stone).setGroup("end_stone_slabs").build();
		RecipeBuilder.make(name + "_brick_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
		RecipeBuilder.make(name + "_brick_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
		
		RecipeBuilder.make(name + "_wall", wall).setOutputCount(6).setShape("###", "###").addMaterial('#', stone).setGroup("end_wall").build();
		RecipeBuilder.make(name + "_brick_wall", brick_wall).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
		
		RecipeBuilder.make(name + "_button", button).setList("#").addMaterial('#', stone).setGroup("end_stone_buttons").build();
		RecipeBuilder.make(name + "_pressure_plate", pressure_plate).setShape("##").addMaterial('#', stone).setGroup("end_stone_plates").build();
		
		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		
		// Block Tags //
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
		TagHelper.addTags(pressure_plate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
	}
}