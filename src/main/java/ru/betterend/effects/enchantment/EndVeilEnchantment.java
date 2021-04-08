package ru.betterend.effects.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.entity.EquipmentSlot;

public class EndVeilEnchantment extends Enchantment {

	public EndVeilEnchantment() {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[] { EquipmentSlot.HEAD });
	}

	@Override
	public boolean isAvailableForRandomSelection() {
		return false;
	}
}
