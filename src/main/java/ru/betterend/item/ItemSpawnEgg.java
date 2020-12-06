package ru.betterend.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class ItemSpawnEgg extends SpawnEggItem implements Patterned {
	public ItemSpawnEgg(EntityType<?> type, int primaryColor, int secondaryColor, Settings settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_SPAWN_EGG, name);
	}
}
