package ru.betterend.recipe;

import net.minecraft.block.Blocks;
import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class SmeltigRecipes {
	public static void register() {
		FurnaceRecipe.make("end_lily_leaf_dried", EndItems.END_LILY_LEAF, EndItems.END_LILY_LEAF_DRIED).build();
		FurnaceRecipe.make("end_glass", EndBlocks.ENDSTONE_DUST, Blocks.GLASS).build();
	}
}
