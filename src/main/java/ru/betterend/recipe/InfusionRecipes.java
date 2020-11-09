package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.recipe.builders.InfusionRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class InfusionRecipes {
	public static void register() {
		InfusionRecipe.Builder.create("runed_flavolite")
			.setInput(EndBlocks.FLAVOLITE.polished)
			.setOutput(EndBlocks.FLAVOLITE_RUNED)
			.addCatalyst(0, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(2, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(4, EndItems.CRYSTAL_SHARDS)
			.addCatalyst(6, EndItems.CRYSTAL_SHARDS)
			.setTime(100)
			.build();
		
		InfusionRecipe.Builder.create("eternal_crystal")
			.setInput(EndItems.CRYSTAL_SHARDS)
			.setOutput(EndItems.ETERNAL_CRYSTAL)
			.addCatalyst(0, Items.END_CRYSTAL)
			.addCatalyst(2, Items.END_CRYSTAL)
			.addCatalyst(4, Items.END_CRYSTAL)
			.addCatalyst(6, Items.END_CRYSTAL)
			.addCatalyst(1, EndItems.ENDER_DUST)
			.addCatalyst(3, EndItems.ENDER_DUST)
			.addCatalyst(5, EndItems.ENDER_DUST)
			.addCatalyst(7, EndItems.ENDER_DUST)
			.setTime(250)
			.build();
	}
}
