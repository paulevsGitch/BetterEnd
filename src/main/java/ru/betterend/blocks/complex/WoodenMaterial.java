package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.*;
import ru.bclib.util.TagHelper;
import ru.betterend.BetterEnd;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;

public class WoodenMaterial {
	public final Block log;
	public final Block bark;

	public final Block log_stripped;
	public final Block bark_stripped;

	public final Block planks;

	public final Block stairs;
	public final Block slab;
	public final Block fence;
	public final Block gate;
	public final Block button;
	public final Block pressurePlate;
	public final Block trapdoor;
	public final Block door;
	
	public final Block craftingTable;
	public final Block ladder;
	public final Block sign;
	
	public final Block chest;
	public final Block barrel;
	public final Block shelf;
	public final Block composter;
	
	public final Tag.Named<Block> logBlockTag;
	public final Tag.Named<Item> logItemTag;
	
	public WoodenMaterial(String name, MaterialColor woodColor, MaterialColor planksColor) {
		FabricBlockSettings materialPlanks = FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).materialColor(planksColor);
		
		log_stripped = EndBlocks.registerBlock(name + "_stripped_log", new BaseRotatedPillarBlock(materialPlanks));
		bark_stripped = EndBlocks.registerBlock(name + "_stripped_bark", new BaseBarkBlock(materialPlanks));
		
		log = EndBlocks.registerBlock(name + "_log", new BaseStripableLogBlock(woodColor, log_stripped));
		bark = EndBlocks.registerBlock(name + "_bark", new StripableBarkBlock(woodColor, bark_stripped));
		
		planks = EndBlocks.registerBlock(name + "_planks", new BaseBlock(materialPlanks));
		stairs = EndBlocks.registerBlock(name + "_stairs", new BaseStairsBlock(planks));
		slab = EndBlocks.registerBlock(name + "_slab", new BaseSlabBlock(planks));
		fence = EndBlocks.registerBlock(name + "_fence", new BaseFenceBlock(planks));
		gate = EndBlocks.registerBlock(name + "_gate", new BaseGateBlock(planks));
		button = EndBlocks.registerBlock(name + "_button", new BaseWoodenButtonBlock(planks));
		pressurePlate = EndBlocks.registerBlock(name + "_plate", new WoodenPressurePlateBlock(planks));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new BaseTrapdoorBlock(planks));
		door = EndBlocks.registerBlock(name + "_door", new BaseDoorBlock(planks));
		
		craftingTable = EndBlocks.registerBlock(name + "_crafting_table", new BaseCraftingTableBlock(planks));
		ladder = EndBlocks.registerBlock(name + "_ladder", new BaseLadderBlock(planks));
		sign = EndBlocks.registerBlock(name + "_sign", new BaseSignBlock(planks));
		
		chest = EndBlocks.registerBlock(name + "_chest", new BaseFenceBlock(planks));
		barrel = EndBlocks.registerBlock(name + "_barrel", new BaseBarrelBlock(planks));
		shelf = EndBlocks.registerBlock(name + "_bookshelf", new BaseBookshelfBlock(planks));
		composter = EndBlocks.registerBlock(name + "_composter", new BaseComposterBlock(planks));
		
		// Recipes //
		GridRecipe.make(name + "_planks", planks).setOutputCount(4).setList("#").addMaterial('#', log, bark, log_stripped, bark_stripped).setGroup("end_planks").build();
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', planks).setGroup("end_planks_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', planks).setGroup("end_planks_slabs").build();
		GridRecipe.make(name + "_fence", fence).setOutputCount(3).setShape("#I#", "#I#").addMaterial('#', planks).addMaterial('I', Items.STICK).setGroup("end_planks_fences").build();
		GridRecipe.make(name + "_gate", gate).setShape("I#I", "I#I").addMaterial('#', planks).addMaterial('I', Items.STICK).setGroup("end_planks_gates").build();
		GridRecipe.make(name + "_button", button).setList("#").addMaterial('#', planks).setGroup("end_planks_buttons").build();
		GridRecipe.make(name + "_pressure_plate", pressurePlate).setShape("##").addMaterial('#', planks).setGroup("end_planks_plates").build();
		GridRecipe.make(name + "_trapdoor", trapdoor).setOutputCount(2).setShape("###", "###").addMaterial('#', planks).setGroup("end_trapdoors").build();
		GridRecipe.make(name + "_door", door).setOutputCount(3).setShape("##", "##", "##").addMaterial('#', planks).setGroup("end_doors").build();
		GridRecipe.make(name + "_crafting_table", craftingTable).setShape("##", "##").addMaterial('#', planks).setGroup("end_tables").build();
		GridRecipe.make(name + "_ladder", ladder).setOutputCount(3).setShape("I I", "I#I", "I I").addMaterial('#', planks).addMaterial('I', Items.STICK).setGroup("end_ladders").build();
		GridRecipe.make(name + "_sign", sign).setOutputCount(3).setShape("###", "###", " I ").addMaterial('#', planks).addMaterial('I', Items.STICK).setGroup("end_signs").build();
		GridRecipe.make(name + "_chest", chest).setShape("###", "# #", "###").addMaterial('#', planks).setGroup("end_chests").build();
		GridRecipe.make(name + "_barrel", barrel).setShape("#S#", "# #", "#S#").addMaterial('#', planks).addMaterial('S', slab).setGroup("end_barrels").build();
		GridRecipe.make(name + "_bookshelf", shelf).setShape("###", "PPP", "###").addMaterial('#', planks).addMaterial('P', Items.BOOK).setGroup("end_bookshelves").build();
		GridRecipe.make(name + "_bark", bark).setShape("##", "##").addMaterial('#', log).setOutputCount(3).build();
		GridRecipe.make(name + "_log", log).setShape("##", "##").addMaterial('#', bark).setOutputCount(3).build();
		GridRecipe.make(name + "_composter", composter).setShape("# #", "# #", "###").addMaterial('#', slab).build();
		GridRecipe.make(name + "_shulker", Items.SHULKER_BOX).setShape("S", "#", "S").addMaterial('S', Items.SHULKER_SHELL).addMaterial('#', chest).build();
		
		// Item Tags //
		TagHelper.addTag(ItemTags.PLANKS, planks);
		TagHelper.addTag(ItemTags.WOODEN_PRESSURE_PLATES, pressurePlate);
		TagHelper.addTag(ItemTags.LOGS, log, bark, log_stripped, bark_stripped);
		TagHelper.addTag(ItemTags.LOGS_THAT_BURN, log, bark, log_stripped, bark_stripped);
		
		TagHelper.addTags(button, ItemTags.WOODEN_BUTTONS, ItemTags.BUTTONS);
		TagHelper.addTags(door, ItemTags.WOODEN_DOORS, ItemTags.DOORS);
		TagHelper.addTags(fence, ItemTags.WOODEN_FENCES, ItemTags.FENCES);
		TagHelper.addTags(slab, ItemTags.WOODEN_SLABS, ItemTags.SLABS);
		TagHelper.addTags(stairs, ItemTags.WOODEN_STAIRS, ItemTags.STAIRS);
		TagHelper.addTags(trapdoor, ItemTags.WOODEN_TRAPDOORS, ItemTags.TRAPDOORS);
		TagHelper.addTag(TagAPI.ITEM_CHEST, chest);
		
		// Block Tags //
		TagHelper.addTag(BlockTags.PLANKS, planks);
		TagHelper.addTag(BlockTags.CLIMBABLE, ladder);
		TagHelper.addTag(BlockTags.LOGS, log, bark, log_stripped, bark_stripped);
		TagHelper.addTag(BlockTags.LOGS_THAT_BURN, log, bark, log_stripped, bark_stripped);
		
		TagHelper.addTags(button, BlockTags.WOODEN_BUTTONS, BlockTags.BUTTONS);
		TagHelper.addTags(door, BlockTags.WOODEN_DOORS, BlockTags.DOORS);
		TagHelper.addTags(fence, BlockTags.WOODEN_FENCES, BlockTags.FENCES);
		TagHelper.addTags(slab, BlockTags.WOODEN_SLABS, BlockTags.SLABS);
		TagHelper.addTags(stairs, BlockTags.WOODEN_STAIRS, BlockTags.STAIRS);
		TagHelper.addTags(trapdoor, BlockTags.WOODEN_TRAPDOORS, BlockTags.TRAPDOORS);
		TagHelper.addTag(TagAPI.BOOKSHELVES, shelf);
		TagHelper.addTag(TagAPI.BLOCK_CHEST, chest);
		
		logBlockTag = TagAPI.makeBlockTag(BetterEnd.MOD_ID, name + "_logs");
		logItemTag = TagAPI.makeItemTag(BetterEnd.MOD_ID, name + "_logs");
		TagHelper.addTag(logBlockTag, log_stripped, bark_stripped, log, bark);
		TagHelper.addTag(logItemTag, log_stripped, bark_stripped, log, bark);
		
		FlammableBlockRegistry.getDefaultInstance().add(log, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(bark, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(log_stripped, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(bark_stripped, 5, 5);
		
		FlammableBlockRegistry.getDefaultInstance().add(planks, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(stairs, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(slab, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(fence, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(gate, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(button, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(pressurePlate, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(trapdoor, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(door, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(craftingTable, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ladder, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(sign, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(chest, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(barrel, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(shelf, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(composter, 5, 20);
	}
	
	public boolean isTreeLog(Block block) {
		return block == log || block == bark;
	}
	
	public boolean isTreeLog(BlockState state) {
		return isTreeLog(state.getBlock());
	}
}