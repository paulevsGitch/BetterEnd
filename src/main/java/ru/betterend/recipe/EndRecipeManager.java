package ru.betterend.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;

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
				if (typeList == null) {
					typeList = Maps.newHashMap();
					result.put(type, typeList);
				}
				for (Entry<Identifier, Recipe<?>> entry : list.entrySet()) {
					Identifier id = entry.getKey();
					if (!typeList.containsKey(id))
						typeList.put(id, entry.getValue());
				}
			}
		}

		return result;
	}

	static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, BetterEnd.getResId(id), serializer);
	}

	static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
		return Registry.register(Registry.RECIPE_TYPE, BetterEnd.getResId(name), new RecipeType<T>() {
			public String toString() {
				return name;
			}
	    });
	}
}