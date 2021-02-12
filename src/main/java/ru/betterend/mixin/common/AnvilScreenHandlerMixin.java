package ru.betterend.mixin.common;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.screen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.tag.BlockTags;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.interfaces.AnvilScreenHandlerExtended;
import ru.betterend.recipe.builders.AnvilRecipe;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler implements AnvilScreenHandlerExtended {

	private List<AnvilRecipe> be_recipes = Collections.emptyList();
	private AnvilRecipe be_currentRecipe;
	private Property anvilLevel;

	public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void be_initAnvilLevel(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo info) {
		int anvLevel = context.run((world, blockPos) -> {
			Block anvilBlock = world.getBlockState(blockPos).getBlock();
			if (anvilBlock instanceof EndAnvilBlock) {
				return ((EndAnvilBlock) anvilBlock).getCraftingLevel();
			}
			return 1;
		}, 1);
		Property anvilLevel = Property.create();
		anvilLevel.set(anvLevel);
		this.anvilLevel = addProperty(anvilLevel);
	}
	
	@Shadow
	public abstract void updateResult();
	
	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void be_canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
		if (be_currentRecipe != null) {
			info.setReturnValue(be_currentRecipe.checkHammerDurability(input, player));
		}
	}
	
	@Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void be_onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (be_currentRecipe != null) {
			this.input.getStack(0).decrement(be_currentRecipe.getInputCount());
			stack = be_currentRecipe.craft(input, player);
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
	public void be_updateOutput(CallbackInfo info) {
		RecipeManager recipeManager = this.player.world.getRecipeManager();
		be_recipes = recipeManager.getAllMatches(AnvilRecipe.TYPE, input, player.world);
		if (be_recipes.size() > 0) {
			int anvilLevel = this.anvilLevel.get();
			be_recipes = be_recipes.stream().filter(recipe ->
					anvilLevel >= recipe.getAnvilLevel()).collect(Collectors.toList());
			if (be_recipes.size() > 0) {
				if (be_currentRecipe == null || !be_recipes.contains(be_currentRecipe)) {
					be_currentRecipe = be_recipes.get(0);
				}
				be_updateResult();
				info.cancel();
			} else {
				be_currentRecipe = null;
			}
		}
	}
	
	@Inject(method = "setNewItemName", at = @At("HEAD"), cancellable = true)
	public void be_setNewItemName(String string, CallbackInfo info) {
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
