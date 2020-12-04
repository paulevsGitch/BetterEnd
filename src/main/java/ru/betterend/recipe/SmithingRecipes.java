package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.recipe.builders.AnvilSmithingRecipe;
import ru.betterend.registry.EndItems;

public class SmithingRecipes {
	public static void register() {
		AnvilSmithingRecipe.Builder.create("ender_pearl_to_dust")
			.setInput(Items.ENDER_PEARL, EndItems.ENDER_SHARD)
			.setOutput(EndItems.ENDER_DUST, 1)
			.setLevel(4)
			.setDamage(5)
			.build();
	}
}
