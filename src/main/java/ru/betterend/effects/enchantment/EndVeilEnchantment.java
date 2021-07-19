package ru.betterend.effects.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EndVeilEnchantment extends Enchantment {
	
	public EndVeilEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[] {EquipmentSlot.HEAD});
	}
	
	@Override
	public boolean isDiscoverable() {
		return false;
	}
}
