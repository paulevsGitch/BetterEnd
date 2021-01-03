package ru.betterend.item;

import net.minecraft.util.Rarity;
import ru.betterend.registry.EndItems;

public class EternalCrystal extends PatternedItem {

	public EternalCrystal() {
		super(EndItems.makeItemSettings().maxCount(16).rarity(Rarity.EPIC));
	}
}