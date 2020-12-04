package ru.betterend.item;

import net.minecraft.item.Item;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class PatternedItem extends Item implements Patterned {
	public PatternedItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
