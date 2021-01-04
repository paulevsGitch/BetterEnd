package ru.betterend.interfaces;

import java.util.List;

import ru.betterend.recipe.builders.AnvilSmithingRecipe;

public interface AnvilScreenHandlerExtended {
	public void be_updateCurrentRecipe(AnvilSmithingRecipe recipe);
	public AnvilSmithingRecipe be_getCurrentRecipe();
	public List<AnvilSmithingRecipe> be_getRecipes();
}
