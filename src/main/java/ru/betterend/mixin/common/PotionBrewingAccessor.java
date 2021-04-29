package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
	@Invoker
	static void callAddMix(Potion input, Item item, Potion output) {
		throw new AssertionError("@Invoker dummy body called");
	}
}
