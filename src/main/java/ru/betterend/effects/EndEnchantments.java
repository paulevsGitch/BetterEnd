package ru.betterend.effects;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import ru.betterend.BetterEnd;
import ru.betterend.effects.enchantment.EndVeilEnchantment;

public class EndEnchantments {
	public final static Enchantment END_VEIL = registerEnchantment("end_veil", new EndVeilEnchantment());
	
	public static Enchantment registerEnchantment(String name, Enchantment enchantment) {
		return Registry.register(Registry.ENCHANTMENT, BetterEnd.makeID(name), enchantment);
	}
	
	public static void register() {}
}
