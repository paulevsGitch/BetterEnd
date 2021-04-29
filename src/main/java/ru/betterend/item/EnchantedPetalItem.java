package ru.betterend.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndItems;

public class EnchantedPetalItem extends PatternedItem {
	public EnchantedPetalItem() {
		super(EndItems.makeItemSettings().rarity(Rarity.RARE).stacksTo(16));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, "item/hydralux_petal");
	}
}
