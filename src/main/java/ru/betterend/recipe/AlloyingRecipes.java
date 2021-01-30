package ru.betterend.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import ru.betterend.recipe.builders.AlloyingRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class AlloyingRecipes {
	public static void register() {
		AlloyingRecipe.Builder.create("additional_iron")
			.setInput(Blocks.IRON_ORE, Blocks.IRON_ORE)
			.setOutput(Items.IRON_INGOT, 3)
			.setExpiriense(2.1F)
			.build();
		AlloyingRecipe.Builder.create("additional_gold")
			.setInput(Blocks.GOLD_ORE, Blocks.GOLD_ORE)
			.setOutput(Items.GOLD_INGOT, 3)
			.setExpiriense(3F)
			.build();
		AlloyingRecipe.Builder.create("additional_netherite")
			.setInput(Blocks.ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS)
			.setOutput(Items.NETHERITE_SCRAP, 3)
			.setExpiriense(6F)
			.setSmeltTime(500)
			.build();
		AlloyingRecipe.Builder.create("terminite_ingot")
			.setInput(Items.IRON_INGOT, EndItems.ENDER_DUST)
			.setOutput(EndBlocks.TERMINITE.ingot, 1)
			.setExpiriense(2.5F)
			.setSmeltTime(450)
			.build();
		AlloyingRecipe.Builder.create("aeternium_ingot")
			.setInput(EndBlocks.TERMINITE.ingot, Items.NETHERITE_INGOT)
			.setOutput(EndItems.AETERNIUM_INGOT, 1)
			.setExpiriense(4.5F)
			.setSmeltTime(600)
			.build();
		AlloyingRecipe.Builder.create("terminite_ingot_thallasium")
			.setInput(EndBlocks.THALLASIUM.ingot, EndItems.ENDER_DUST)
			.setOutput(EndBlocks.TERMINITE.ingot, 1)
			.setExpiriense(2.5F)
			.setSmeltTime(450)
			.build();
	}
}
