package ru.betterend.effects.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class EndVeilEnchantment extends Enchantment {

	public EndVeilEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[] { EquipmentSlot.HEAD });
	}
	
	@Override
	public boolean isAvailableForRandomSelection() {
		return false;
	}
}
