package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.registry.ItemRegistry;

public class SmithingRecipes {
	public static void register() {
		AnvilSmithingRecipe.Builder.create("ender_pearl_to_dust")
			.setInput(Items.ENDER_PEARL)
			.setOutput(ItemRegistry.ENDER_DUST, 1)
			.setLevel(4)
			.setDamage(5)
			.build();
	}
}
