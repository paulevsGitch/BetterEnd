package ru.betterend.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndArmorItem extends ArmorItem implements Patterned {
	public EndArmorItem(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
		super(material, slot, settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
