package ru.betterend.item;

import net.minecraft.item.Item;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndItems;

public class EternalCrystal extends Item implements Patterned {

	public EternalCrystal() {
		super(EndItems.makeSettings());
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}