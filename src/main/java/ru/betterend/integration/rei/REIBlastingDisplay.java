package ru.betterend.integration.rei;

import net.minecraft.world.item.crafting.BlastingRecipe;

public class REIBlastingDisplay extends REIAlloyingDisplay {
	public REIBlastingDisplay(BlastingRecipe recipe) {
		super(recipe, recipe.getExperience(), recipe.getCookingTime());
	}
}
