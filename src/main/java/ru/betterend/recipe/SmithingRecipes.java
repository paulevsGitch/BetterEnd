package ru.betterend.recipe;

import net.minecraft.item.Items;
import ru.betterend.recipe.builders.AnvilSmithingRecipe;
import ru.betterend.registry.EndItems;

public class SmithingRecipes {
	public static void register() {
		AnvilSmithingRecipe.Builder.create("ender_pearl_to_dust")
			.setInput(Items.ENDER_PEARL)
			.setOutput(EndItems.ENDER_DUST, 1)
			.setLevel(4)
			.setDamage(5)
			.build();
		AnvilSmithingRecipe.Builder.create("ender_shard_to_dust")
			.setInput(EndItems.ENDER_SHARD)
			.setOutput(EndItems.ENDER_DUST, 1)
			.setLevel(2)
			.setDamage(3)
			.build();
		
		AnvilSmithingRecipe.Builder.create("aeternium_axe_head")
			.setInput(EndItems.AETERNIUM_INGOT)
			.setOutput(EndItems.AETERNIUM_AXE_HEAD, 1)
			.setLevel(4)
			.setDamage(6)
			.build();
		AnvilSmithingRecipe.Builder.create("aeternium_pickaxe_head")
			.setInput(EndItems.AETERNIUM_INGOT)
			.setOutput(EndItems.AETERNIUM_PICKAXE_HEAD, 1)
			.setLevel(4)
			.setDamage(6)
			.build();
		AnvilSmithingRecipe.Builder.create("aeternium_shovel_head")
			.setInput(EndItems.AETERNIUM_INGOT)
			.setOutput(EndItems.AETERNIUM_SHOVEL_HEAD, 1)
			.setLevel(4)
			.setDamage(6)
			.build();
		AnvilSmithingRecipe.Builder.create("aeternium_hoe_head")
			.setInput(EndItems.AETERNIUM_INGOT)
			.setOutput(EndItems.AETERNIUM_HOE_HEAD, 1)
			.setLevel(4)
			.setDamage(6)
			.build();
	}
}
