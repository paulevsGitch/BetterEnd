package ru.betterend.item;

import net.minecraft.world.item.Rarity;
import ru.betterend.registry.EndItems;

public class EternalCrystalItem extends ModelProviderItem {
	public EternalCrystalItem() {
		super(EndItems.makeEndItemSettings().stacksTo(16).rarity(Rarity.EPIC));
	}
}