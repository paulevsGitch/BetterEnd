package ru.betterend.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndItems;

public class EnchantedPetalItem extends PatternedItem {
	public EnchantedPetalItem() {
		super(EndItems.makeItemSettings().rarity(Rarity.RARE).maxCount(16));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, "item/hydralux_petal");
	}
}
