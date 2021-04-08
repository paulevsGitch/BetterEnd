package ru.betterend.effects;

import net.minecraft.world.entity.effect.StatusEffect;
import net.minecraft.world.entity.effect.StatusEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.mixin.common.BrewingAccessor;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class EndPotions {
	public final static Potion END_VEIL = registerPotion("end_veil", EndStatusEffects.END_VEIL, 3600);
	public final static Potion LONG_END_VEIL = registerPotion("long_end_veil", EndStatusEffects.END_VEIL, 9600);

	public static Potion registerPotion(String name, StatusEffect effect, int duration) {
		return registerPotion(name,
				new Potion(name, new StatusEffectInstance[] { new StatusEffectInstance(effect, duration) }));
	}

	public static Potion registerPotion(String name, Potion potion) {
		return Registry.register(Registry.POTION, BetterEnd.makeID(name), potion);
	}

	public static void register() {
		BrewingAccessor.callRegisterPotionRecipe(Potions.AWKWARD, EndItems.ENDER_DUST, END_VEIL);
		BrewingAccessor.callRegisterPotionRecipe(END_VEIL, Items.REDSTONE, LONG_END_VEIL);
		BrewingAccessor.callRegisterPotionRecipe(Potions.AWKWARD, EndBlocks.MURKWEED.asItem(), Potions.NIGHT_VISION);
	}
}
