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
		String rusultStr = JsonHelper.getString(json, "result");
		Identifier resultId = new Identifier(rusultStr);
		ItemStack output = new ItemStack(Registry.ITEM.getOrEmpty(resultId).orElseThrow(() -> {
			return new IllegalStateException("Item: " + rusultStr + " does not exist");
		}));
		float experience = JsonHelper.getFloat(json, "experience", 0.0F);
		int smeltTime = JsonHelper.getInt(json, "smelttime", 350);
		
		return new AlloyingRecipe(id, primaryInput, secondaryInput, output, experience, smeltTime);
	}

	@Override
	public AlloyingRecipe read(Identifier id, PacketByteBuf packetBuffer) {
		Ingredient primaryInput = Ingredient.fromPacket(packetBuffer);
		Ingredient secondaryInput = Ingredient.fromPacket(packetBuffer);
		ItemStack output = packetBuffer.readItemStack();
		float experience = packetBuffer.readFloat();
		int smeltTime = packetBuffer.readVarInt();
		
		return new AlloyingRecipe(id, primaryInput, secondaryInput, output, experience, smeltTime);
	}

	@Override
	public void write(PacketByteBuf packetBuffer, AlloyingRecipe recipe) {
		recipe.primaryInput.write(packetBuffer);
		recipe.secondaryInput.write(packetBuffer);
		packetBuffer.writeItemStack(recipe.output);
		packetBuffer.writeFloat(recipe.experience);
		packetBuffer.writeInt(recipe.smeltTime);
	}

}
