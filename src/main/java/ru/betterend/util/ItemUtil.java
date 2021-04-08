package ru.betterend.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.JsonHelper;
import net.minecraft.core.Registry;
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
			if (parts.length < 2)
				return null;
			if (parts.length == 2) {
				ResourceLocation itemId = new ResourceLocation(stackString);
				Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
					return new IllegalStateException("Output item " + itemId + " does not exists!");
				});
				return new ItemStack(item);
			}
			ResourceLocation itemId = new ResourceLocation(parts[0], parts[1]);
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
		try {
			if (!recipe.has("item")) {
				throw new IllegalStateException("Invalid JsonObject. Entry 'item' does not exists!");
			}
			ResourceLocation itemId = new ResourceLocation(JsonHelper.getString(recipe, "item"));
			Item item = Registry.ITEM.getOrEmpty(itemId).orElseThrow(() -> {
				return new IllegalStateException("Output item " + itemId + " does not exists!");
			});
			int count = JsonHelper.getInt(recipe, "count", 1);
			return new ItemStack(item, count);
		} catch (Exception ex) {
			BetterEnd.LOGGER.error("ItemStack deserialization error!", ex);
		}
		return null;
	}
}
