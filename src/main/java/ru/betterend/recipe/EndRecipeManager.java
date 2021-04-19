package ru.betterend.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ru.betterend.BetterEnd;

public class EndRecipeManager {
	private static final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> RECIPES = Maps.newHashMap();

	public static void addRecipe(RecipeType<?> type, Recipe<?> recipe) {
		Map<ResourceLocation, Recipe<?>> list = RECIPES.get(type);
		if (list == null) {
			list = Maps.newHashMap();
			RECIPES.put(type, list);
		}
		list.put(recipe.getId(), recipe);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Recipe<?>> T getRecipe(RecipeType<T> type, ResourceLocation id) {
		if (RECIPES.containsKey(type)) {
			return (T) RECIPES.get(type).get(id);
		}
		return null;
	}

	public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getMap(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes) {
		Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> result = Maps.newHashMap();

		for (RecipeType<?> type : recipes.keySet()) {
			Map<ResourceLocation, Recipe<?>> typeList = Maps.newHashMap();
			typeList.putAll(recipes.get(type));
			result.put(type, typeList);
		}

		for (RecipeType<?> type : RECIPES.keySet()) {
			Map<ResourceLocation, Recipe<?>> list = RECIPES.get(type);
			if (list != null) {
				Map<ResourceLocation, Recipe<?>> typeList = result.get(type);
				if (typeList == null) {
					typeList = Maps.newHashMap();
					result.put(type, typeList);
				}
				for (Entry<ResourceLocation, Recipe<?>> entry : list.entrySet()) {
					ResourceLocation id = entry.getKey();
					if (!typeList.containsKey(id))
						typeList.put(id, entry.getValue());
				}
			}
		}

		return result;
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, BetterEnd.getStringId(id), serializer);
	}

	public static <T extends Recipe<?>> RecipeType<T> registerType(String type) {
		ResourceLocation recipeTypeId = BetterEnd.makeID(type);
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<T>() {
			public String toString() {
				return type;
			}
	    });
	}
}