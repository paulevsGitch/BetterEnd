package ru.betterend.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class AlloyingRecipeSerializer implements RecipeSerializer<AlloyingRecipe> {

	@Override
	public AlloyingRecipe read(Identifier id, JsonObject json) {
		JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
		Ingredient primaryInput = Ingredient.fromJson(ingredients.get(0));
		Ingredient secondaryInput = Ingredient.fromJson(ingredients.get(1));
		String resultStr = JsonHelper.getString(json, "result");
		String group = JsonHelper.getString(json, "group", "");
		Identifier resultId = new Identifier(resultStr);
		ItemStack output = new ItemStack(Registry.ITEM.getOrEmpty(resultId).orElseThrow(() -> {
			return new IllegalStateException("Item: " + resultStr + " does not exists!");
		}));
		float experience = JsonHelper.getFloat(json, "experience", 0.0F);
		int smeltTime = JsonHelper.getInt(json, "smelttime", 350);
		
		return new AlloyingRecipe(id, group, primaryInput, secondaryInput, output, experience, smeltTime);
	}

	@Override
	public AlloyingRecipe read(Identifier id, PacketByteBuf packetBuffer) {
		Identifier recipeId = packetBuffer.readIdentifier();
		return (AlloyingRecipe) EndRecipeManager.getRecipe(AlloyingRecipe.TYPE, recipeId);
	}

	@Override
	public void write(PacketByteBuf packetBuffer, AlloyingRecipe recipe) {
		packetBuffer.writeIdentifier(recipe.id);
	}
}
