package ru.betterend.recipe;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.recipes.FurnaceRecipe;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class FurnaceRecipes {
	public static void register() {
		FurnaceRecipe.make(BetterEnd.MOD_ID, "end_lily_leaf_dried", EndItems.END_LILY_LEAF, EndItems.END_LILY_LEAF_DRIED).checkConfig(Configs.RECIPE_CONFIG).build();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "end_glass", EndBlocks.ENDSTONE_DUST, Blocks.GLASS).checkConfig(Configs.RECIPE_CONFIG).build();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "end_berry", EndItems.SHADOW_BERRY_RAW, EndItems.SHADOW_BERRY_COOKED).checkConfig(Configs.RECIPE_CONFIG).buildFoodlike();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "end_fish", EndItems.END_FISH_RAW, EndItems.END_FISH_COOKED).checkConfig(Configs.RECIPE_CONFIG).buildFoodlike();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "slime_ball", EndBlocks.JELLYSHROOM_CAP_PURPLE, Items.SLIME_BALL).checkConfig(Configs.RECIPE_CONFIG).build();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "menger_sponge", EndBlocks.MENGER_SPONGE_WET, EndBlocks.MENGER_SPONGE).checkConfig(Configs.RECIPE_CONFIG).build();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "chorus_mushroom", EndItems.CHORUS_MUSHROOM_RAW, EndItems.CHORUS_MUSHROOM_COOKED).checkConfig(Configs.RECIPE_CONFIG).buildFoodlike();
		FurnaceRecipe.make(BetterEnd.MOD_ID, "bolux_mushroom", EndBlocks.BOLUX_MUSHROOM, EndItems.BOLUX_MUSHROOM_COOKED).checkConfig(Configs.RECIPE_CONFIG).buildFoodlike();
	}
}
