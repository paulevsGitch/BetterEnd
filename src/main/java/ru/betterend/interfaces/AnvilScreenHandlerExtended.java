package ru.betterend.interfaces;

import java.util.List;

import ru.betterend.recipe.builders.AnvilRecipe;

public interface AnvilScreenHandlerExtended {
	public void be_updateCurrentRecipe(AnvilRecipe recipe);
	public AnvilRecipe be_getCurrentRecipe();
	public List<AnvilRecipe> be_getRecipes();
	
	default void be_nextRecipe() {
		List<AnvilRecipe> recipes = this.be_getRecipes();
		AnvilRecipe current = this.be_getCurrentRecipe();
		int i = recipes.indexOf(current) + 1;
		if (i >= recipes.size()) {
			i = 0;
		}
		this.be_updateCurrentRecipe(recipes.get(i));
	}
	
	default void be_previousRecipe() {
		List<AnvilRecipe> recipes = this.be_getRecipes();
		AnvilRecipe current = this.be_getCurrentRecipe();
		int i = recipes.indexOf(current) - 1;
		if (i <= 0) {
			i = recipes.size() - 1;
		}
		this.be_updateCurrentRecipe(recipes.get(i));
	}
}
