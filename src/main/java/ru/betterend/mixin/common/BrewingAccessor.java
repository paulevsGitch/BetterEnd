package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingAccessor {
	@Invoker
	static void callRegisterPotionRecipe(Potion input, Item item, Potion output) {
		throw new AssertionError("@Invoker dummy body called");
	}
}
