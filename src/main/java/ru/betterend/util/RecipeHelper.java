package ru.betterend.util;

import net.minecraft.core.Registry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class RecipeHelper {
	public static boolean exists(ItemLike item) {
		if (item instanceof Block) {
			return Registry.BLOCK.getKey((Block) item) != Registry.BLOCK.getDefaultKey();
		}
		else {
			return Registry.ITEM.getKey(item.asItem()) != Registry.ITEM.getDefaultKey();
		}
	}

	public static boolean exists(ItemLike... items) {
		for (ItemLike item : items) {
			if (!exists(item)) {
				return false;
			}
		}
		return true;
	}
}
