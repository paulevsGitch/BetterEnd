package ru.betterend.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import ru.betterend.blocks.basis.BarkBlock;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.blocks.basis.EndBarrelBlock;
import ru.betterend.blocks.basis.EndBlockStripableLogLog;
import ru.betterend.blocks.basis.EndBookshelfBlock;
import ru.betterend.blocks.basis.EndChestBlock;
import ru.betterend.blocks.basis.EndComposterBlock;
import ru.betterend.blocks.basis.EndCraftingTableBlock;
import ru.betterend.blocks.basis.EndDoorBlock;
import ru.betterend.blocks.basis.EndFenceBlock;
import ru.betterend.blocks.basis.EndGateBlock;
import ru.betterend.blocks.basis.EndLadderBlock;
import ru.betterend.blocks.basis.EndPillarBlock;
import ru.betterend.blocks.basis.EndSignBlock;
import ru.betterend.blocks.basis.EndSlabBlock;
import ru.betterend.blocks.basis.EndStairsBlock;
import ru.betterend.blocks.basis.EndTrapdoorBlock;
import ru.betterend.blocks.basis.EndWoodenButtonBlock;
import ru.betterend.blocks.basis.EndWoodenPlateBlock;
import ru.betterend.blocks.basis.StrippableBarkBlock;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.TagHelper;

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

	public final Tag.Identified<Block> logBlockTag;
	public final Tag.Identified<Item> logItemTag;

	public WoodenMaterial(String name, MaterialColor woodColor, MaterialColor planksColor) {
		FabricBlockSettings materialPlanks = FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).materialColor(planksColor);

		log_stripped = EndBlocks.registerBlock(name + "_stripped_log", new EndPillarBlock(materialPlanks));
		bark_stripped = EndBlocks.registerBlock(name + "_stripped_bark", new BarkBlock(materialPlanks));

		log = EndBlocks.registerBlock(name + "_log", new EndBlockStripableLogLog(woodColor, log_stripped));
		bark = EndBlocks.registerBlock(name + "_bark", new StrippableBarkBlock(woodColor, bark_stripped));

		planks = EndBlocks.registerBlock(name + "_planks", new BlockBase(materialPlanks));
		stairs = EndBlocks.registerBlock(name + "_stairs", new EndStairsBlock(planks));
		slab = EndBlocks.registerBlock(name + "_slab", new EndSlabBlock(planks));
		fence = EndBlocks.registerBlock(name + "_fence", new EndFenceBlock(planks));
		gate = EndBlocks.registerBlock(name + "_gate", new EndGateBlock(planks));
		button = EndBlocks.registerBlock(name + "_button", new EndWoodenButtonBlock(planks));
		pressurePlate = EndBlocks.registerBlock(name + "_plate", new EndWoodenPlateBlock(planks));
		trapdoor = EndBlocks.registerBlock(name + "_trapdoor", new EndTrapdoorBlock(planks));
		door = EndBlocks.registerBlock(name + "_door", new EndDoorBlock(planks));

		craftingTable = EndBlocks.registerBlock(name + "_crafting_table", new EndCraftingTableBlock(planks));
		ladder = EndBlocks.registerBlock(name + "_ladder", new EndLadderBlock(planks));
		sign = EndBlocks.registerBlock(name + "_sign", new EndSignBlock(planks));

		chest = EndBlocks.registerBlock(name + "_chest", new EndChestBlock(planks));
		barrel = EndBlocks.registerBlock(name + "_barrel", new EndBarrelBlock(planks));
		shelf = EndBlocks.registerBlock(name + "_bookshelf", new EndBookshelfBlock(planks));
		composter = EndBlocks.registerBlock(name + "_composter", new EndComposterBlock(planks));

		// Recipes //
		GridRecipe.make(name + "_planks", planks).setOutputCount(4).setList("#")
				.addMaterial('#', log, bark, log_stripped, bark_stripped).setGroup("end_planks").build();
		GridRecipe.make(name + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###")
				.addMaterial('#', planks).setGroup("end_planks_stairs").build();
		GridRecipe.make(name + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', planks)
				.setGroup("end_planks_slabs").build();
		GridRecipe.make(name + "_fence", fence).setOutputCount(3).setShape("#I#", "#I#").addMaterial('#', planks)
				.addMaterial('I', Items.STICK).setGroup("end_planks_fences").build();
		GridRecipe.make(name + "_gate", gate).setShape("I#I", "I#I").addMaterial('#', planks)
				.addMaterial('I', Items.STICK).setGroup("end_planks_gates").build();
		GridRecipe.make(name + "_button", button).setList("#").addMaterial('#', planks).setGroup("end_planks_buttons")
				.build();
		GridRecipe.make(name + "_pressure_plate", pressurePlate).setShape("##").addMaterial('#', planks)
				.setGroup("end_planks_plates").build();
		GridRecipe.make(name + "_trapdoor", trapdoor).setOutputCount(2).setShape("###", "###").addMaterial('#', planks)
				.setGroup("end_trapdoors").build();
		GridRecipe.make(name + "_door", door).setOutputCount(3).setShape("##", "##", "##").addMaterial('#', planks)
				.setGroup("end_doors").build();
		GridRecipe.make(name + "_crafting_table", craftingTable).setShape("##", "##").addMaterial('#', planks)
				.setGroup("end_tables").build();
		GridRecipe.make(name + "_ladder", ladder).setOutputCount(3).setShape("I I", "I#I", "I I")
				.addMaterial('#', planks).addMaterial('I', Items.STICK).setGroup("end_ladders").build();
		GridRecipe.make(name + "_sign", sign).setOutputCount(3).setShape("###", "###", " I ").addMaterial('#', planks)
				.addMaterial('I', Items.STICK).setGroup("end_signs").build();
		GridRecipe.make(name + "_chest", chest).setShape("###", "# #", "###").addMaterial('#', planks)
				.setGroup("end_chests").build();
		GridRecipe.make(name + "_barrel", barrel).setShape("#S#", "# #", "#S#").addMaterial('#', planks)
				.addMaterial('S', slab).setGroup("end_barrels").build();
		GridRecipe.make(name + "_bookshelf", shelf).setShape("###", "PPP", "###").addMaterial('#', planks)
				.addMaterial('P', Items.BOOK).setGroup("end_bookshelves").build();
		GridRecipe.make(name + "_bark", bark).setShape("##", "##").addMaterial('#', log).setOutputCount(3).build();
		GridRecipe.make(name + "_log", log).setShape("##", "##").addMaterial('#', bark).setOutputCount(3).build();
		GridRecipe.make(name + "_composter", composter).setShape("# #", "# #", "###").addMaterial('#', slab).build();
		GridRecipe.make(name + "_shulker", Items.SHULKER_BOX).setShape("S", "#", "S")
				.addMaterial('S', Items.SHULKER_SHELL).addMaterial('#', chest).build();

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
		TagHelper.addTag(EndTags.ITEM_CHEST, chest);

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
		TagHelper.addTag(EndTags.BOOKSHELVES, shelf);
		TagHelper.addTag(EndTags.BLOCK_CHEST, chest);

		logBlockTag = EndTags.makeBlockTag(name + "_logs");
		logItemTag = EndTags.makeItemTag(name + "_logs");
		TagHelper.addTag(logBlockTag, log_stripped, bark_stripped, log, bark);
		TagHelper.addTag(logItemTag, log_stripped, bark_stripped, log, bark);
	}

	public boolean isTreeLog(Block block) {
		return block == log || block == bark;
	}

	public boolean isTreeLog(BlockState state) {
		return isTreeLog(state.getBlock());
	}
}