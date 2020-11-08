package ru.betterend.recipe;

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
			.build();
	}
}
