package ru.betterend.integration.rei;

import net.minecraft.world.item.crafting.BlastingRecipe;
import ru.betterend.recipe.builders.AlloyingRecipe;

public class REIBlastingDisplay extends REIAlloyingDisplay{
    public REIBlastingDisplay(BlastingRecipe recipe) {
        super(recipe, recipe.getExperience(), recipe.getCookingTime());
    }
}
