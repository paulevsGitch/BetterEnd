package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import ru.betterend.interfaces.BetterEndRecipe;
import ru.betterend.recipe.builders.AlloyingRecipe;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
	@Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
	private static void be_getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cinfo) {
		if (recipe instanceof AlloyingRecipe) {
			cinfo.setReturnValue(RecipeBookGroup.BLAST_FURNACE_MISC);
		} else if (recipe instanceof BetterEndRecipe) {
			cinfo.setReturnValue(RecipeBookGroup.UNKNOWN);
		}
	}
}
