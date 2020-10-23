package ru.betterend.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;

public abstract class ItemSpawnEgg extends SpawnEggItem {
	public ItemSpawnEgg(EntityType<?> type, int primaryColor, int secondaryColor, Settings settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
}
