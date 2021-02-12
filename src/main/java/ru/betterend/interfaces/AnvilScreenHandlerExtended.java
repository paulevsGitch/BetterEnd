package ru.betterend.interfaces;

import java.util.List;

import ru.betterend.recipe.builders.AnvilRecipe;

public interface AnvilScreenHandlerExtended {
	void be_updateCurrentRecipe(AnvilRecipe recipe);
	AnvilRecipe be_getCurrentRecipe();
	List<AnvilRecipe> be_getRecipes();
	
	default void be_nextRecipe() {
		List<AnvilRecipe> recipes = be_getRecipes();
		if (recipes.size() < 2) return;
		AnvilRecipe current = be_getCurrentRecipe();
		int i = recipes.indexOf(current) + 1;
		if (i >= recipes.size()) {
			i = 0;
		}
		be_updateCurrentRecipe(recipes.get(i));
	}
	
	default void be_previousRecipe() {
		List<AnvilRecipe> recipes = be_getRecipes();
		if (recipes.size() < 2) return;
		AnvilRecipe current = be_getCurrentRecipe();
		int i = recipes.indexOf(current) - 1;
		if (i <= 0) {
			i = recipes.size() - 1;
		}
		be_updateCurrentRecipe(recipes.get(i));
	}
}
