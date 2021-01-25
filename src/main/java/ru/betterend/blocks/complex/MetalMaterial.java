package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import ru.betterend.blocks.BulbVineLanternBlock;
import ru.betterend.blocks.BulbVineLanternColoredBlock;
import ru.betterend.blocks.ChandelierBlock;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.blocks.basis.EndChainBlock;
import ru.betterend.blocks.basis.EndDoorBlock;
import ru.betterend.blocks.basis.EndMetalPaneBlock;
import ru.betterend.blocks.basis.EndSlabBlock;
import ru.betterend.blocks.basis.EndStairsBlock;
import ru.betterend.blocks.basis.EndTrapdoorBlock;
import ru.betterend.blocks.basis.EndWoodenPlateBlock;
import ru.betterend.item.EndAxeItem;
import ru.betterend.item.EndHoeItem;
import ru.betterend.item.EndPickaxeItem;
import ru.betterend.item.EndShovelItem;
import ru.betterend.item.EndSwordItem;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.TagHelper;

public class MetalMaterial {
	public final Block ore;
	public final Block block;
	public final Block tile;
	public final Block bars;
	public final Block plate;
	public final Block door;
	public final Block trapdoor;
	public final Block anvil;
	public final Block chain;
	public final Block stairs;
	public final Block slab;
	
	public final Block chandelier;
	public final Block bulb_lantern;
	public final ColoredMaterial bulb_lantern_colored;
	
	public final Item nugget;
	public final Item ingot;
	public final Item shovel;
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	
	public MetalMaterial(String name, MaterialColor color, ToolMaterial material) {
		FabricBlockSettings materialBlock = FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color);
		FabricBlockSettings lantern = FabricBlockSettings.copyOf(materialBlock).sounds(BlockSoundGroup.LANTERN).hardness(1).resistance(1).luminance(15);
		
		ore = EndBlocks.registerBlock(name + "_ore", new BlockBase(FabricBlockSettings.copyOf(Blocks.END_STONE)));
		block = EndBlocks.registerBlock(name + "_block", new BlockBase(materialBlock));
		tile = EndBlocks.registerBlock(name + "_tile", new BlockBase(materialBlock));
		stairs = EndBlocks.registerBlock(name + "_stairs", new EndStairsBlock(tile));
		slab = EndBlocks.registerBlock(name + "_slab", new EndSlabBlock(tile));
		door = EndBlocks.registerBlock(name + "_door", new EndDoorBlock(block));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new EndTrapdoorBlock(block));
		anvil = EndBlocks.registerBlock(name + "_anvil", new EndAnvilBlock(color));
		bars = EndBlocks.registerBlock(name + "_bars", new EndMetalPaneBlock(block));
		chain = EndBlocks.registerBlock(name + "_chain", new EndChainBlock(color));
		plate = EndBlocks.registerBlock(name + "_plate", new EndWoodenPlateBlock(block));
		
		chandelier = EndBlocks.registerBlock(name + "_chandelier", new ChandelierBlock(materialBlock));
		bulb_lantern = EndBlocks.registerBlock(name + "_bulb_lantern", new BulbVineLanternBlock(lantern));
		bulb_lantern_colored = new ColoredMaterial(BulbVineLanternColoredBlock::new, bulb_lantern, false);
		
		nugget = EndItems.registerItem(name + "_nugget");
		ingot = EndItems.registerItem(name + "_ingot");
		shovel = EndItems.registerTool(name + "_shovel", new EndShovelItem(material, 1.5F, -3.0F, EndItems.makeItemSettings()));
		sword = EndItems.registerTool(name + "_sword", new EndSwordItem(material, 3, -2.4F, EndItems.makeItemSettings()));
		pickaxe = EndItems.registerTool(name + "_pickaxe", new EndPickaxeItem(material, 1, -2.8F, EndItems.makeItemSettings()));
		axe = EndItems.registerTool(name + "_axe", new EndAxeItem(material, 6.0F, -3.0F, EndItems.makeItemSettings()));
		hoe = EndItems.registerTool(name + "_hoe", new EndHoeItem(material, -3, 0.0F, EndItems.makeItemSettings()));
		
		FurnaceRecipe.make("thallasium_ingot_furnace", ore, ingot).build(true);
		AlloyingRecipe.Builder.create("thallasium_ingot").setInput(ore, ore).setOutput(ingot, 3).setExpiriense(2.1F).build();
		
		GridRecipe.make(name + "_ingot_from_nuggets", ingot).setShape("###", "###", "###").addMaterial('#', nugget).setGroup("end_metal_ingots_nug").build();
		GridRecipe.make(name + "_block", block).setShape("###", "###", "###").addMaterial('#', ingot).setGroup("end_metal_blocks").build();
		GridRecipe.make(name + "_ingot_from_block", ingot).setOutputCount(9).setList("#").addMaterial('#', block).setGroup("end_metal_ingots").build();
		
		GridRecipe.make(name + "_tile", tile).setOutputCount(4).setShape("##", "##").addMaterial('#', block).setGroup("end_metal_tiles").build();
		GridRecipe.make(name + "_bars", bars).setOutputCount(16).setShape("###", "###").addMaterial('#', ingot).setGroup("end_metal_bars").build();
		GridRecipe.make(name + "_plate", plate).setShape("##").addMaterial('#', ingot).setGroup("end_metal_plates").build();
		GridRecipe.make(name + "_door", door).setOutputCount(3).setOutputCount(16).setShape("##", "##", "##").addMaterial('#', ingot).setGroup("end_metal_doors").build();
		GridRecipe.make(name + "_trapdoor", trapdoor).setShape("##", "##").addMaterial('#', ingot).setGroup("end_metal_trapdoors").build();
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', block, tile).setGroup("end_metal_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', block, tile).setGroup("end_metal_slabs").build();
		GridRecipe.make(name + "_chain", chain).setShape("N", "#", "N").addMaterial('#', ingot).addMaterial('N', nugget).setGroup("end_metal_chain").build();
		GridRecipe.make(name + "_anvil", anvil).setOutputCount(3).setShape("###", " I ", "III").addMaterial('#', block, tile).addMaterial('I', ingot).setGroup("end_metal_anvil").build();
		GridRecipe.make(name + "bulb_lantern", bulb_lantern).setShape("C", "I", "#").addMaterial('C', chain).addMaterial('I', ingot).addMaterial('#', EndItems.GLOWING_BULB).build();
		
		GridRecipe.make(name + "_axe", axe).setShape("##", "#I", " I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_hoe", hoe).setShape("##", " I", " I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_pickaxe", pickaxe).setShape("###", " I ", " I ").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_sword", sword).setShape("#", "#", "I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		
		GridRecipe.make(name + "_chandelier", chandelier).setShape("I#I", " # ").addMaterial('#', ingot).addMaterial('I', EndItems.LUMECORN_ROD).setGroup("end_metal_chandelier").build();
		
		FurnaceRecipe.make(name + "_axe_ingot", axe, nugget).build(true);
		FurnaceRecipe.make(name + "_hoe_ingot", hoe, nugget).build(true);
		FurnaceRecipe.make(name + "_pickaxe_ingot", pickaxe, nugget).build(true);
		FurnaceRecipe.make(name + "_sword_ingot", sword, nugget).build(true);
		
		TagHelper.addTag(BlockTags.ANVIL, anvil);
	}
}