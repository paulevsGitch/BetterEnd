package ru.betterend.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.registry.Registry;

public class RecipeHelper {
	public static boolean exists(ItemConvertible item) {
		if (item instanceof Block) {
			return Registry.BLOCK.getId((Block) item) != Registry.BLOCK.getDefaultId();
		}
		else {
			return Registry.ITEM.getId(item.asItem()) != Registry.ITEM.getDefaultId();
		}
	}
}
