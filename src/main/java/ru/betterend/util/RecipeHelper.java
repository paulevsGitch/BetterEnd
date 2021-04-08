package ru.betterend.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemConvertible;
import net.minecraft.core.Registry;

public class RecipeHelper {
	public static boolean exists(ItemConvertible item) {
		if (item instanceof Block) {
			return Registry.BLOCK.getKey((Block) item) != Registry.BLOCK.getDefaultId();
		} else {
			return Registry.ITEM.getId(item.asItem()) != Registry.ITEM.getDefaultId();
		}
	}

	public static boolean exists(ItemConvertible... items) {
		for (ItemConvertible item : items) {
			if (!exists(item)) {
				return false;
			}
		}
		return true;
	}
}
