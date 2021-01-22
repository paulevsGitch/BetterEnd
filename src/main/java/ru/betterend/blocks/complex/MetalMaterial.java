package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.blocks.basis.EndChainBlock;
import ru.betterend.blocks.basis.EndDoorBlock;
import ru.betterend.blocks.basis.EndMetalPaneBlock;
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
	
	public final Item ingot;
	public final Item shovel;
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	
	public MetalMaterial(String name, MaterialColor color, ToolMaterial material) {
		FabricBlockSettings materialBlock = FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color);
		
		ore = EndBlocks.registerBlock(name + "_ore", new BlockBase(FabricBlockSettings.copyOf(Blocks.END_STONE)));
		block = EndBlocks.registerBlock(name + "_block", new BlockBase(materialBlock));
		tile = EndBlocks.registerBlock(name + "_tile", new BlockBase(materialBlock));
		bars = EndBlocks.registerBlock(name + "_bars", new EndMetalPaneBlock(block));
		plate = EndBlocks.registerBlock(name + "_plate", new EndWoodenPlateBlock(block));
		door = EndBlocks.registerBlock(name + "_door", new EndDoorBlock(block));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new EndTrapdoorBlock(block));
		anvil = EndBlocks.registerBlock(name + "_anvil", new EndAnvilBlock(color));
		chain = EndBlocks.registerBlock(name + "_chain", new EndChainBlock(color));
		
		ingot = EndItems.registerItem(name + "_ingot");
		shovel = EndItems.registerTool(name + "_shovel", new EndShovelItem(material, 1.0F, -3.0F, EndItems.makeItemSettings()));
		sword = EndItems.registerTool(name + "_sword", new EndSwordItem(material, 2, -2.4F, EndItems.makeItemSettings()));
		pickaxe = EndItems.registerTool(name + "_pickaxe", new EndPickaxeItem(material, 1, -2.8F, EndItems.makeItemSettings()));
		axe = EndItems.registerTool(name + "_axe", new EndAxeItem(material, 5.0F, -3.0F, EndItems.makeItemSettings()));
		hoe = EndItems.registerTool(name + "_hoe", new EndHoeItem(material, -3, 0.0F, EndItems.makeItemSettings()));
		
		FurnaceRecipe.make("thallasium_ingot_furnace", ore, ingot).build();
		AlloyingRecipe.Builder.create("thallasium_ingot").setInput(ore, ore).setOutput(ingot, 3).setExpiriense(2.1F).build();
		
		GridRecipe.make(name + "_block", block).setShape("###", "###", "###").addMaterial('#', ingot).build();
		GridRecipe.make(name + "_ingot_from_block", ingot).setOutputCount(9).setList("#").addMaterial('#', block).build();
		
		GridRecipe.make(name + "_tile", tile).setOutputCount(4).setShape("##", "##").addMaterial('#', block).build();
		GridRecipe.make(name + "_bars", bars).setOutputCount(16).setShape("###", "###").addMaterial('#', ingot).build();
		GridRecipe.make(name + "_plate", plate).setShape("##").addMaterial('#', ingot).build();
		GridRecipe.make(name + "_door", door).setOutputCount(3).setOutputCount(16).setShape("##", "##", "##").addMaterial('#', ingot).build();
		GridRecipe.make(name + "_trapdoor", trapdoor).setShape("##", "##").addMaterial('#', ingot).build();
		
		GridRecipe.make(name + "_axe", axe).setShape("##", "#I", " I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_hoe", hoe).setShape("##", " I", " I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_pickaxe", pickaxe).setShape("###", " I ", " I ").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		GridRecipe.make(name + "_sword", sword).setShape("#", "#", "I").addMaterial('#', ingot).addMaterial('I', Items.STICK).build();
		
		TagHelper.addTag(BlockTags.ANVIL, anvil);
	}
}