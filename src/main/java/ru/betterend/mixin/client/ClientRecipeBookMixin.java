package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import ru.betterend.interfaces.BetterEndRecipe;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
	@Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
	private static void be_getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> info) {
		if (recipe instanceof BetterEndRecipe) {
			info.setReturnValue(RecipeBookCategories.UNKNOWN);
		}
	}
}
