package ru.betterend.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import ru.betterend.recipe.builders.FurnaceRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class FurnaceRecipes {
	public static void register() {
		FurnaceRecipe.make("end_lily_leaf_dried", EndItems.END_LILY_LEAF, EndItems.END_LILY_LEAF_DRIED).build();
		FurnaceRecipe.make("end_glass", EndBlocks.ENDSTONE_DUST, Blocks.GLASS).build();
		FurnaceRecipe.make("end_berry", EndItems.SHADOW_BERRY_RAW, EndItems.SHADOW_BERRY_COOKED).build();
		FurnaceRecipe.make("end_fish", EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED).build();
		FurnaceRecipe.make("slime_ball", EndBlocks.JELLYSHROOM_CAP_PURPLE, Items.SLIME_BALL).build();
		FurnaceRecipe.make("thallasium_ingot_furnace", EndBlocks.THALLASIUM_ORE, EndItems.THALLASIUM_INGOT).build();
	}
}
