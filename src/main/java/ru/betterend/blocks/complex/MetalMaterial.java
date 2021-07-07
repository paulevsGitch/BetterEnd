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
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BaseChainBlock;
import ru.bclib.blocks.BaseDoorBlock;
import ru.bclib.blocks.BaseMetalBarsBlock;
import ru.bclib.blocks.BaseSlabBlock;
import ru.bclib.blocks.BaseStairsBlock;
import ru.bclib.blocks.BaseTrapdoorBlock;
import ru.bclib.blocks.WoodenPressurePlateBlock;
import ru.bclib.items.ModelProviderItem;
import ru.bclib.items.tool.BaseAxeItem;
import ru.bclib.items.tool.BaseHoeItem;
import ru.bclib.items.tool.BasePickaxeItem;
import ru.bclib.items.tool.BaseShovelItem;
import ru.bclib.items.tool.BaseSwordItem;
import ru.bclib.recipes.FurnaceRecipe;
import ru.bclib.recipes.GridRecipe;
import ru.bclib.recipes.SmithingTableRecipe;
import ru.bclib.util.TagHelper;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BulbVineLanternBlock;
import ru.betterend.blocks.BulbVineLanternColoredBlock;
import ru.betterend.blocks.ChandelierBlock;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.config.Configs;
import ru.betterend.item.EndAnvilItem;
import ru.betterend.item.EndArmorItem;
import ru.betterend.item.tool.EndHammerItem;
import ru.betterend.item.tool.EndPickaxe;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.recipe.builders.AnvilRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.registry.EndTags;

public class MetalMaterial {
	public final Block ore;
	public final Block block;
	public final Block tile;
	public final Block bars;
	public final Block pressurePlate;
	public final Block door;
	public final Block trapdoor;
	public final Block chain;
	public final Block stairs;
	public final Block slab;
	
	public final Block chandelier;
	public final Block bulb_lantern;
	public final ColoredMaterial bulb_lantern_colored;

	public final Block anvilBlock;
	public final Item anvilItem;

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
		return new MetalMaterial(name, true, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color), EndItems.makeEndItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeNormal(String name, MaterialColor color, float hardness, float resistance, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, true, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color).hardness(hardness).resistance(resistance), EndItems.makeEndItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeOreless(String name, MaterialColor color, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, false, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color), EndItems.makeEndItemSettings(), material, armor);
	}
	
	public static MetalMaterial makeOreless(String name, MaterialColor color, float hardness, float resistance, Tier material, ArmorMaterial armor) {
		return new MetalMaterial(name, false, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).materialColor(color).hardness(hardness).resistance(resistance), EndItems.makeEndItemSettings(), material, armor);
	}
	
	private MetalMaterial(String name, boolean hasOre, FabricBlockSettings settings, Properties itemSettings, Tier material, ArmorMaterial armor) {
		BlockBehaviour.Properties lanternProperties = FabricBlockSettings.copyOf(settings).hardness(1).resistance(1).luminance(15).sound(SoundType.LANTERN);
		final int level = material.getLevel();
		
		ore = hasOre ? EndBlocks.registerBlock(name + "_ore", new BaseBlock(FabricBlockSettings.copyOf(Blocks.END_STONE))) : null;
		block = EndBlocks.registerBlock(name + "_block", new BaseBlock(settings));
		tile = EndBlocks.registerBlock(name + "_tile", new BaseBlock(settings));
		stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock(tile));
		slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock(tile));
		door = EndBlocks.registerBlock(name + "_door", new BaseDoorBlock(block));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new BaseTrapdoorBlock(block));
		bars = EndBlocks.registerBlock(name + "_bars", new BaseMetalBarsBlock(block));
		chain = EndBlocks.registerBlock(name + "_chain", new BaseChainBlock(block.defaultMaterialColor()));
		pressurePlate = EndBlocks.registerBlock(name + "_plate", new WoodenPressurePlateBlock(block));
		
		chandelier = EndBlocks.registerBlock(name + "_chandelier", new ChandelierBlock(block));
		bulb_lantern = EndBlocks.registerBlock(name + "_bulb_lantern", new BulbVineLanternBlock(lanternProperties));
		bulb_lantern_colored = new ColoredMaterial(BulbVineLanternColoredBlock::new, bulb_lantern, false);

		nugget = EndItems.registerEndItem(name + "_nugget", new ModelProviderItem(itemSettings));
		ingot = EndItems.registerEndItem(name + "_ingot", new ModelProviderItem(itemSettings));
		
		shovelHead = EndItems.registerEndItem(name + "_shovel_head");
		pickaxeHead = EndItems.registerEndItem(name + "_pickaxe_head");
		axeHead = EndItems.registerEndItem(name + "_axe_head");
		hoeHead = EndItems.registerEndItem(name + "_hoe_head");
		swordBlade = EndItems.registerEndItem(name + "_sword_blade");
		swordHandle = EndItems.registerEndItem(name + "_sword_handle");
		
		shovel = EndItems.registerEndTool(name + "_shovel", new BaseShovelItem(material, 1.5F, -3.0F, itemSettings));
		sword = EndItems.registerEndTool(name + "_sword", new BaseSwordItem(material, 3, -2.4F, itemSettings));
		pickaxe = EndItems.registerEndTool(name + "_pickaxe", new EndPickaxe(material, 1, -2.8F, itemSettings));
		axe = EndItems.registerEndTool(name + "_axe", new BaseAxeItem(material, 6.0F, -3.0F, itemSettings));
		hoe = EndItems.registerEndTool(name + "_hoe", new BaseHoeItem(material, -3, 0.0F, itemSettings));
		hammer = EndItems.registerEndTool(name + "_hammer", new EndHammerItem(material, 5.0F, -3.2F, 0.3D, itemSettings));
		
		forgedPlate = EndItems.registerEndItem(name + "_forged_plate");
		helmet = EndItems.registerEndItem(name + "_helmet", new EndArmorItem(armor, EquipmentSlot.HEAD, itemSettings));
		chestplate = EndItems.registerEndItem(name + "_chestplate", new EndArmorItem(armor, EquipmentSlot.CHEST, itemSettings));
		leggings = EndItems.registerEndItem(name + "_leggings", new EndArmorItem(armor, EquipmentSlot.LEGS, itemSettings));
		boots = EndItems.registerEndItem(name + "_boots", new EndArmorItem(armor, EquipmentSlot.FEET, itemSettings));

		anvilBlock = EndBlocks.registerBlock(name + "_anvil", new EndAnvilBlock(this, block.defaultMaterialColor(), level));
		anvilItem = EndItems.registerEndItem(name + "_anvil_item", new EndAnvilItem(anvilBlock));
		
		if (hasOre) {
			FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_ingot_furnace", ore, ingot).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_ingot").buildWithBlasting();
			AlloyingRecipe.Builder.create(name + "_ingot_alloy").setInput(ore, ore).setOutput(ingot, 3).setExpiriense(2.1F).build();
		}
		
		// Basic recipes
		GridRecipe.make(BetterEnd.MOD_ID, name + "_ingot_from_nuggets", ingot).checkConfig(Configs.RECIPE_CONFIG).setShape("###", "###", "###").addMaterial('#', nugget).setGroup("end_metal_ingots_nug").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_nuggets_from_ingot", nugget).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(9).setList("#").addMaterial('#', ingot).setGroup("end_metal_nuggets_ing").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_block", block).checkConfig(Configs.RECIPE_CONFIG).setShape("###", "###", "###").addMaterial('#', ingot).setGroup("end_metal_blocks").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_ingot_from_block", ingot).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(9).setList("#").addMaterial('#', block).setGroup("end_metal_ingots").build();
		
		// Block recipes
		GridRecipe.make(BetterEnd.MOD_ID, name + "_tile", tile).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("##", "##").addMaterial('#', block).setGroup("end_metal_tiles").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bars", bars).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(16).setShape("###", "###").addMaterial('#', ingot).setGroup("end_metal_bars").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_pressure_plate", pressurePlate).checkConfig(Configs.RECIPE_CONFIG).setShape("##").addMaterial('#', ingot).setGroup("end_metal_plates").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_door", door).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(3).setShape("##", "##", "##").addMaterial('#', ingot).setGroup("end_metal_doors").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_trapdoor", trapdoor).checkConfig(Configs.RECIPE_CONFIG).setShape("##", "##").addMaterial('#', ingot).setGroup("end_metal_trapdoors").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_stairs", stairs).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', block, tile).setGroup("end_metal_stairs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_slab", slab).checkConfig(Configs.RECIPE_CONFIG).setOutputCount(6).setShape("###").addMaterial('#', block, tile).setGroup("end_metal_slabs").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_chain", chain).checkConfig(Configs.RECIPE_CONFIG).setShape("N", "#", "N").addMaterial('#', ingot).addMaterial('N', nugget).setGroup("end_metal_chain").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_anvil", anvilBlock).checkConfig(Configs.RECIPE_CONFIG).setShape("###", " I ", "III").addMaterial('#', block, tile).addMaterial('I', ingot).setGroup("end_metal_anvil").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_bulb_lantern", bulb_lantern).checkConfig(Configs.RECIPE_CONFIG).setShape("C", "I", "#").addMaterial('C', chain).addMaterial('I', ingot).addMaterial('#', EndItems.GLOWING_BULB).build();
		
		GridRecipe.make(BetterEnd.MOD_ID, name + "_chandelier", chandelier).checkConfig(Configs.RECIPE_CONFIG).setShape("I#I", " # ").addMaterial('#', ingot).addMaterial('I', EndItems.LUMECORN_ROD).setGroup("end_metal_chandelier").build();
		
		// Tools & armor into nuggets
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_axe_nugget", axe, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_hoe_nugget", hoe, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_pickaxe_nugget", pickaxe, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_sword_nugget", sword, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_hammer_nugget", hammer, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_helmet_nugget", helmet, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_chestplate_nugget", chestplate, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_leggings_nugget", leggings, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		FurnaceRecipe.make(BetterEnd.MOD_ID, name + "_boots_nugget", boots, nugget).checkConfig(Configs.RECIPE_CONFIG).setGroup("end_nugget").buildWithBlasting();
		
		// Tool parts from ingots
		AnvilRecipe.Builder.create(name + "_shovel_head").setInput(ingot).setOutput(shovelHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_pickaxe_head").setInput(ingot).setInputCount(3).setOutput(pickaxeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_axe_head").setInput(ingot).setInputCount(3).setOutput(axeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_hoe_head").setInput(ingot).setInputCount(2).setOutput(hoeHead).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_sword_blade").setInput(ingot).setOutput(swordBlade).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		AnvilRecipe.Builder.create(name + "_forged_plate").setInput(ingot).setOutput(forgedPlate).setAnvilLevel(level).setToolLevel(level).setDamage(level).build();
		
		// Tools from parts
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_hammer").checkConfig(Configs.RECIPE_CONFIG).setResult(hammer).setBase(block).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_axe").checkConfig(Configs.RECIPE_CONFIG).setResult(axe).setBase(axeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_pickaxe").checkConfig(Configs.RECIPE_CONFIG).setResult(pickaxe).setBase(pickaxeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_hoe").checkConfig(Configs.RECIPE_CONFIG).setResult(hoe).setBase(hoeHead).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_sword_handle").checkConfig(Configs.RECIPE_CONFIG).setResult(swordHandle).setBase(ingot).setAddition(Items.STICK).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_sword").checkConfig(Configs.RECIPE_CONFIG).setResult(sword).setBase(swordBlade).setAddition(swordHandle).build();
		SmithingTableRecipe.create(BetterEnd.MOD_ID, name + "_shovel").checkConfig(Configs.RECIPE_CONFIG).setResult(shovel).setBase(shovelHead).setAddition(Items.STICK).build();
		
		// Armor crafting
		GridRecipe.make(BetterEnd.MOD_ID, name + "_helmet", helmet).checkConfig(Configs.RECIPE_CONFIG).setShape("###", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_helmets").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_chestplate", chestplate).checkConfig(Configs.RECIPE_CONFIG).setShape("# #", "###", "###").addMaterial('#', forgedPlate).setGroup("end_metal_chestplates").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_leggings", leggings).checkConfig(Configs.RECIPE_CONFIG).setShape("###", "# #", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_leggings").build();
		GridRecipe.make(BetterEnd.MOD_ID, name + "_boots", boots).checkConfig(Configs.RECIPE_CONFIG).setShape("# #", "# #").addMaterial('#', forgedPlate).setGroup("end_metal_boots").build();
		
		TagHelper.addTag(BlockTags.ANVIL, anvilBlock);
		TagHelper.addTag(BlockTags.BEACON_BASE_BLOCKS, block);
		TagHelper.addTag(ItemTags.BEACON_PAYMENT_ITEMS, ingot);
		TagHelper.addTag(TagAPI.DRAGON_IMMUNE, ore, bars);
	}
}