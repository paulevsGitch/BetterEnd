package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
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
import ru.betterend.item.EndArmorItem;
import ru.betterend.item.ModelProviderItem;
import ru.betterend.item.tool.EndAxeItem;
import ru.betterend.item.tool.EndHammerItem;
import ru.betterend.item.tool.EndHoeItem;
import ru.betterend.item.tool.EndPickaxeItem;
import ru.betterend.item.tool.EndShovelItem;
import ru.betterend.item.tool.EndSwordItem;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.recipe.builders.SmithingTableRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.registry.EndTags;
import ru.betterend.util.TagHelper;

public class MetalMaterial {
	public final Block ore;
	public final Block block;
	public final Block tile;
	public final Block bars;
	public final Block pressurePlate;
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
	
	public final Item shovelHead;
	public final Item pickaxeHead;
	public final Item axeHead;
	public final Item hoeHead;
	public final Item swordBlade;
	public final Item swordHandle;
	
	public final Item shovel;
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item hammer;
	
	public final Item forgedPlate;
	public final Item helmet;
	public final Item chestplate;
	public final Item leggings;
	public final Item boots;
	
	public static MetalMaterial makeNormal(String name, MaterialColor color, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, true, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color), EndItems.makeItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeNormal(String name, MaterialColor color, float hardness, float resistance, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, true, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color).hardness(hardness).resistance(resistance), EndItems.makeItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeOreless(String name, MaterialColor color, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, false, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color), EndItems.makeItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeOreless(String name, MaterialColor color, float hardness, float resistance, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, false, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color).hardness(hardness).resistance(resistance), EndItems.makeItemSettings(), material, armor);
	}
	
	private MetalMaterial(String name, boolean hasOre, FabricBlockSettings settings, Properties itemSettings, Tier material, ArmorMaterial armor) {
		BlockBehaviour.Properties lanternProperties = FabricBlockSettings.copyOf(settings).hardness(1).resistance(1).luminance(15).sound(SoundType.LANTERN);
		final int level = material.getLevel();
		
		ore = hasOre ? EndBlocks.registerBlock(name + "_ore", new BlockBase(FabricBlockSettings.copyOf(Blocks.END_STONE))) : null;
		block = EndBlocks.registerBlock(name + "_block", new BlockBase(settings));
		tile = EndBlocks.registerBlock(name + "_tile", new BlockBase(settings));
		stairs = EndBlocks.registerBlock(name + "_stairs", new EndStairsBlock(tile));
		slab = EndBlocks.registerBlock(name + "_slab", new EndSlabBlock(tile));
		door = EndBlocks.registerBlock(name + "_door", new EndDoorBlock(block));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new EndTrapdoorBlock(block));
		anvil = EndBlocks.registerBlock(name + "_anvil", new EndAnvilBlock(block.defaultMaterialColor(), level));
		bars = EndBlocks.registerBlock(name + "_bars", new EndMetalPaneBlock(block));
		chain = EndBlocks.registerBlock(name + "_chain", new EndChainBlock(block.defaultMaterialColor()));
		pressurePlate = EndBlocks.registerBlock(name + "_plate", new EndWoodenPlateBlock(block));
		
		chandelier = EndBlocks.registerBlock(name + "_chandelier", new ChandelierBlock(block));
		bulb_lantern = EndBlocks.registerBlock(name + "_bulb_lantern", new BulbVineLanternBlock(lanternProperties));
		bulb_lantern_colored = new ColoredMaterial(BulbVineLanternColoredBlock::new, bulb_lantern, false);
		
		nugget = EndItems.registerItem(name + "_nugget", new ModelProviderItem(itemSettings));
		ingot = EndItems.registerItem(name + "_ingot", new ModelProviderItem(itemSettings));
		
		shovelHead = EndItems.registerItem(name + "_shovel_head");
		pickaxeHead = EndItems.registerItem(name + "_pickaxe_head");
		axeHead = EndItems.registerItem(name + "_axe_head");
		hoeHead = EndItems.registerItem(name + "_hoe_head");
		swordBlade = EndItems.registerItem(name + "_sword_blade");
		swordHandle = EndItems.registerItem(name + "_sword_handle");
		
		shovel = EndItems.registerTool(name + "_shovel", new EndShovelItem(material, 1.5F, -3.0F, itemSettings));
		sword = EndItems.registerTool(name + "_sword", new EndSwordItem(material, 3, -2.4F, itemSettings));
		pickaxe = EndItems.registerTool(name + "_pickaxe", new EndPickaxeItem(material, 1, -2.8F, itemSettings));
		axe = EndItems.registerTool(name + "_axe", new EndAxeItem(material, 6.0F, -3.0F, itemSettings));
		hoe = EndItems.registerTool(name + "_hoe", new EndHoeItem(material, -3, 0.0F, itemSettings));
		hammer = EndItems.registerTool(name + "_hammer", new EndHammerItem(material, 5.0F, -3.2F, 0.3D, itemSettings));
		
		forgedPlate = EndItems.registerItem(name + "_forged_plate");
		helmet = EndItems.registerItem(name + "_helmet", new EndArmorItem(armor, EquipmentSlot.HEAD, itemSettings));
		chestplate = EndItems.registerItem(name + "_chestplate", new EndArmorItem(armor, EquipmentSlot.CHEST, itemSettings));
		leggings = EndItems.registerItem(name + "_leggings", new EndArmorItem(armor, EquipmentSlot.LEGS, itemSettings));
		boots = EndItems.registerItem(name + "_boots", new EndArmorItem(armor, EquipmentSlot.FEET, itemSettings));
		
		if (hasOre) {
			FurnaceRecipe.make(name + "_ingot_furnace", ore, ingot).setGroup("end_ingot").buildWithBlasting();
			AlloyingRecipe.Builder.create(name + "_ingot_alloy").setInput(ore, ore).setOutput(ingot, 3).setExpiriense(2.1F).build();
		}
		
		// Basic recipes
		GridRecipe.make(name + "_ingot_from_nuggets", ingot).setShape("###", "###", "###").addMaterial('#', nugget).setGroup("end_metal_ingots_nug").build();
		GridRecipe.make(name + "_nuggets_from_ingot", nugget).setOutputCount(9).setList("#").addMaterial('#', ingot).setGroup("end_metal_nuggets_ing").build();
		GridRecipe.make(name + "_block", block).setShape("###", "###", "###").addMaterial('#', ingot).setGroup("end_metal_blocks").build();
		GridRecipe.make(name + "_ingot_from_block", ingot).setOutputCount(9).setList("#").addMaterial('#', block).setGroup("end_metal_ingots").build();
		
		// Block recipes
		GridRecipe.make(name + "_tile", tile).setOutputCount(4).setShape("##", "##").addMaterial('#', block).setGroup("end_metal_tiles").build();
		GridRecipe.make(name + "_bars", bars).setOutputCount(16).setShape("###", "###").addMaterial('#', ingot).setGroup("end_metal_bars").build();
		GridRecipe.make(name + "_pressure_plate", pressurePlate).setShape("##").addMaterial('#', ingot).setGroup("end_metal_plates").build();
		GridRecipe.make(name + "_door", door).setOutputCount(3).setShape("##", "##", "##").addMaterial('#', ingot).setGroup("end_metal_doors").build();
		GridRecipe.make(name + "_trapdoor", trapdoor).setShape("##", "##").addMaterial('#', ingot).setGroup("end_metal_trapdoors").build();
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', block, tile).setGroup("end_metal_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', block, tile).setGroup("end_metal_slabs").build();
		GridRecipe.make(name + "_chain", chain).setShape("N", "#", "N").addMaterial('#', ingot).addMaterial('N', nugget).setGroup("end_metal_chain").build();
		GridRecipe.make(name + "_anvil", anvil).setShape("###", " I ", "III").addMaterial('#', block, tile).addMaterial('I', ingot).setGroup("end_metal_anvil").build();
		GridRecipe.make(name + "_bulb_lantern", bulb_lantern).setShape("C", "I", "#").addMaterial('C', chain).addMaterial('I', ingot).addMaterial('#', EndItems.GLOWING_BULB).build();
		
		GridRecipe.make(name + "_chandelier", chandelier).setShape("I#I", " # ").addMaterial('#', ingot).addMaterial('I', EndItems.LUMECORN_ROD).setGroup("end_metal_chandelier").build();
		
		// Tools & armor into nuggets
		FurnaceRecipe.make(name + "_axe_nugget", axe, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_hoe_nugget", hoe, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_pickaxe_nugget", pickaxe, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_sword_nugget", sword, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_hammer_nugget", hammer, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_helmet_nugget", helmet, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_chestplate_nugget", chestplate, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_leggings_nugget", leggings, nugget).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(name + "_boots_nugget", boots, nugget).setGroup("end_nugget").buildWithBlasting();
		
		// Tool parts from ingots
		AnvilRecipe.Builder.create(name + "_shovel_head").setInput(ingot).setOutput(shovelHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_pickaxe_head").setInput(ingot).setInputCount(3).setOutput(pickaxeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_axe_head").setInput(ingot).setInputCount(3).setOutput(axeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_hoe_head").setInput(ingot).setInputCount(2).setOutput(hoeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_sword_blade").setInput(ingot).setOutput(swordBlade).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_forged_plate").setInput(ingot).setOutput(forgedPlate).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		
		// Tools from parts
		SmithingTableRecipe.create(name + "_hammer").setResult(hammer).setBase(block).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(name + "_axe").setResult(axe).setBase(axeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(name + "_pickaxe").setResult(pickaxe).setBase(pickaxeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(name + "_hoe").setResult(hoe).setBase(hoeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(name + "_sword_handle").setResult(swordHandle).setBase(ingot).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(name + "_sword").setResult(sword).setBase(swordBlade).setAddition(swordHandle).build();
		SmithingTableRecipe.create(name + "_shovel").setResult(shovel).setBase(shovelHead).setAddition(Items.STICK).build();
		
		// Armor crafting
		GridRecipe.make(name + "_helmet", helmet).setShape("###", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_helmets").build();
		GridRecipe.make(name + "_chestplate", chestplate).setShape("# #", "###", "###").addMaterial('#', forgedPlate).setGroup("end_metal_chestplates").build();
		GridRecipe.make(name + "_leggings", leggings).setShape("###", "# #", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_leggings").build();
		GridRecipe.make(name + "_boots", boots).setShape("# #", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_boots").build();
		
		TagHelper.addTag(BlockTags.ANVIL, anvil);
		TagHelper.addTag(BlockTags.BEACON_BASE_BLOCKS, block);
		TagHelper.addTag(ItemTags.BEACON_PAYMENT_ITEMS, ingot);
		TagHelper.addTag(EndTags.DRAGON_IMMUNE, ore, bars);
	}
}