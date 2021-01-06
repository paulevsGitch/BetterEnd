package ru.betterend.mixin.common;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.BlockTags;
import ru.betterend.interfaces.AnvilScreenHandlerExtended;
import ru.betterend.recipe.builders.AnvilRecipe;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler implements AnvilScreenHandlerExtended {

	private List<AnvilRecipe> be_recipes = Collections.emptyList();
	private AnvilRecipe be_currentRecipe;
	
	public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}
	
	@Shadow
	public abstract void updateResult();
	
	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
		if (be_currentRecipe != null) {
			ItemStack output = this.be_currentRecipe.craft(input, player);
			if (!output.isEmpty()) {
				info.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (be_currentRecipe != null) {
			this.input.getStack(0).decrement(1);
			this.onContentChanged(input);
			this.context.run((world, blockPos) -> {
				BlockState anvilState = world.getBlockState(blockPos);
				if (!player.abilities.creativeMode && anvilState.isIn(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
					BlockState landingState = AnvilBlock.getLandingState(anvilState);
					if (landingState == null) {
						world.removeBlock(blockPos, false);
						world.syncWorldEvent(1029, blockPos, 0);
					} else {
						world.setBlockState(blockPos, landingState, 2);
						world.syncWorldEvent(1030, blockPos, 0);
					}
				} else {
					world.syncWorldEvent(1030, blockPos, 0);
				}
			});
			info.setReturnValue(stack);
		}
	}
	
	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	public void updateOutput(CallbackInfo info) {
		RecipeManager recipeManager = this.player.world.getRecipeManager();
		this.be_recipes = recipeManager.getAllMatches(AnvilRecipe.TYPE, input, player.world);
		if (be_recipes.size() > 0) {
			this.be_currentRecipe = recipeManager.getFirstMatch(AnvilRecipe.TYPE, input, player.world).get();
			this.be_updateResult();
			info.cancel();
		}
	}
	
	@Inject(method = "setNewItemName", at = @At("HEAD"), cancellable = true)
	public void setNewItemName(String string, CallbackInfo info) {
		if (be_currentRecipe != null) {
			info.cancel();
		}
	}
	
	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == 0) {
			this.be_previousRecipe();
			return true;
		} else if (id == 1) {
			this.be_nextRecipe();
			return true;
		}
		return super.onButtonClick(player, id);
	}
	
	private void be_updateResult() {
		if (be_currentRecipe == null) return;
		this.output.setStack(0, be_currentRecipe.craft(input));
		this.sendContentUpdates();
	}
	
	@Override
	public void be_updateCurrentRecipe(AnvilRecipe recipe) {
		this.be_currentRecipe = recipe;
		this.be_updateResult();
	}
	
	@Override
	public AnvilRecipe be_getCurrentRecipe() {
		return this.be_currentRecipe;
	}
	
	@Override
	public List<AnvilRecipe> be_getRecipes() {
		return this.be_recipes;
	}
}
