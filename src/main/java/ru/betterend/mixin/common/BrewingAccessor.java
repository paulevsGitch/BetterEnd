package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingAccessor {
	@Invoker
	static void callRegisterPotionRecipe(Potion input, Item item, Potion output) {
		throw new AssertionError("@Invoker dummy body called");
	}
}
