package ru.betterend.item;

import net.minecraft.world.item.Item;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class PatternedItem extends Item implements Patterned {
	public PatternedItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public String getModelString(String name) {
		return Patterns.createItemGenerated(name);
	}
}
