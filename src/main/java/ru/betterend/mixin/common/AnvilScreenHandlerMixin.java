package ru.betterend.mixin.common;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.world.World;
import ru.betterend.recipe.AnvilSmithingRecipe;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

	private final World world = this.player.world;
	private final RecipeManager recipeManager = this.world.getRecipeManager();
	private AnvilSmithingRecipe currentRecipe;
	
	@Final
	@Shadow
	private Property levelCost;
	
	public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}
	
	@Inject(method = "canTakeOutput", at = @At("RETURN"))
	protected void canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValue()) {
			info.setReturnValue(currentRecipe != null && !currentRecipe.craft(input, player).isEmpty());
		}
	}
	
	@Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (currentRecipe != null) {
			this.input.getStack(0).decrement(1);
			if (!currentRecipe.matches(input)) {
				this.currentRecipe = null;
			}
			info.cancel();
		}
	}
	
	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	public void updateResult(CallbackInfo info) {
		this.currentRecipe = null;
		List<AnvilSmithingRecipe> recipes = this.recipeManager.listAllOfType(AnvilSmithingRecipe.TYPE);
		for (AnvilSmithingRecipe entry : recipes) {
			if (entry.matches(input)) {
				this.currentRecipe = entry;
				break;
			}
		}
		if (currentRecipe != null) {
			this.levelCost.set(0);
			this.output.setStack(0, currentRecipe.getOutput());
			this.sendContentUpdates();
			info.cancel();
		}
	}
}
