package ru.betterend.recipe;

import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.registry.ItemRegistry;

public class SmeltigRecipes {
	public static void register() {
		FurnaceRecipe.make("end_lily_leaf_dried", ItemRegistry.END_LILY_LEAF, ItemRegistry.END_LILY_LEAF_DRIED).build();
	}
}
