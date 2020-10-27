package ru.betterend.recipe;

import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.registry.EndItems;

public class SmeltigRecipes {
	public static void register() {
		FurnaceRecipe.make("end_lily_leaf_dried", EndItems.END_LILY_LEAF, EndItems.END_LILY_LEAF_DRIED).build();
	}
}
