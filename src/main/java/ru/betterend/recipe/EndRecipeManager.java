package ru.betterend.recipe;

import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class EndRecipeManager {
	private static final Map<RecipeType<?>, Map<Identifier, Recipe<?>>> RECIPES = Maps.newHashMap();

	public static void addRecipe(RecipeType<?> type, Recipe<?> recipe) {
		Map<Identifier, Recipe<?>> list = RECIPES.get(type);
		if (list == null) {
			list = Maps.newHashMap();
			RECIPES.put(type, list);
		}
		list.put(recipe.getId(), recipe);
	}

	public static Map<RecipeType<?>, Map<Identifier, Recipe<?>>> getMap(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes) {
		Map<RecipeType<?>, Map<Identifier, Recipe<?>>> result = Maps.newHashMap();

		for (RecipeType<?> type : recipes.keySet()) {
			Map<Identifier, Recipe<?>> typeList = Maps.newHashMap();
			typeList.putAll(recipes.get(type));
			result.put(type, typeList);
		}

		for (RecipeType<?> type : RECIPES.keySet()) {
			Map<Identifier, Recipe<?>> list = RECIPES.get(type);
			if (list != null) {
				Map<Identifier, Recipe<?>> typeList = result.get(type);
				list.forEach((id, recipe) -> {
					if (!typeList.containsKey(id))
						typeList.put(id, recipe);
				});
			}
		}

		return result;
	}
}