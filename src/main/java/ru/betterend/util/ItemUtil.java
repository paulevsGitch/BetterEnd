package ru.betterend.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;

public class ItemUtil {
	
	public static String toStackString(@NotNull ItemStack stack) {
		try {
			if (stack == null) {
				throw new IllegalStateException("Stack can't be null!");
			}
			Item item = stack.getItem();
			return Registry.ITEM.getId(item) + ":" + stack.getCount();
		} catch (Exception ex) {
			BetterEnd.LOGGER.error("ItemStack serialization error!", ex);
		}
		return "";
	}
	
	@Nullable
	public static ItemStack fromStackString(String stackString) {
		if (stackString == null || stackString.equals("")) {
			return null;
		}
		try {
			String[] parts = stackString.split(":");
			if (parts.length < 2) return null;
			if (parts.length == 2) {
				Identifier itemId = new Identifier(stackString);
				Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
					return new IllegalStateException("Output item " + itemId + " does not exists!");
				});
				return new ItemStack(item);
			}
			Identifier itemId = new Identifier(parts[0], parts[1]);
			Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
				return new IllegalStateException("Output item " + itemId + " does not exists!");
			});
			return new ItemStack(item, Integer.valueOf(parts[2]));
		} catch (Exception ex) {
			BetterEnd.LOGGER.error("ItemStack deserialization error!", ex);
		}
		return null;
	}
	
	@Nullable
	public static ItemStack fromJsonRecipe(JsonObject recipe) {
		if (!recipe.has("item")) return null;
		try {
			Identifier itemId = new Identifier(JsonHelper.getString(recipe, "item"));
			Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
				return new IllegalStateException("Output item " + itemId + " does not exists!");
			});
			if (item == null) return null;
			int count = JsonHelper.getInt(recipe, "count", 1);
			return new ItemStack(item, count);
		} catch (Exception ex) {
			BetterEnd.LOGGER.error("ItemStack deserialization error!", ex);
		}
		return null;
	}
}
