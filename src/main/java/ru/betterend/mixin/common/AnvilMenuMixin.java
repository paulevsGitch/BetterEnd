package ru.betterend.mixin.common;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.interfaces.AnvilScreenHandlerExtended;
import ru.betterend.recipe.builders.AnvilRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu implements AnvilScreenHandlerExtended {
	private List<AnvilRecipe> be_recipes = Collections.emptyList();
	private AnvilRecipe be_currentRecipe;
	private DataSlot anvilLevel;
	
	public AnvilMenuMixin(int syncId, Inventory playerInventory) {
		super(MenuType.ANVIL, syncId, playerInventory, ContainerLevelAccess.NULL);
	}
	
	@Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
	public void be_initAnvilLevel(int syncId, Inventory inventory, ContainerLevelAccess context, CallbackInfo info) {
		this.anvilLevel = addDataSlot(DataSlot.standalone());
		if (context != ContainerLevelAccess.NULL) {
			int level = context.evaluate((world, blockPos) -> {
				Block anvilBlock = world.getBlockState(blockPos).getBlock();
				if (anvilBlock instanceof EndAnvilBlock) {
					return ((EndAnvilBlock) anvilBlock).getCraftingLevel();
				}
				return 1;
			}, 1);
			anvilLevel.set(level);
		}
		else {
			anvilLevel.set(1);
		}
	}
	
	@Shadow
	public abstract void createResult();
	
	@Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
	protected void be_canTakeOutput(Player player, boolean present, CallbackInfoReturnable<Boolean> info) {
		if (be_currentRecipe != null) {
			info.setReturnValue(be_currentRecipe.checkHammerDurability(inputSlots, player));
		}
	}
	
	@Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
	protected void be_onTakeOutput(Player player, ItemStack stack, CallbackInfo info) {
		if (be_currentRecipe != null) {
			inputSlots.getItem(0).shrink(be_currentRecipe.getInputCount());
			stack = be_currentRecipe.craft(inputSlots, player);
			slotsChanged(inputSlots);
			access.execute((world, blockPos) -> {
				BlockState anvilState = world.getBlockState(blockPos);
				if (!player.getAbilities().instabuild && anvilState.is(BlockTags.ANVIL) && player.getRandom().nextDouble() < 0.1) {
					BlockState landingState = EndAnvilBlock.applyDamage(anvilState);
					if (landingState == null) {
						world.removeBlock(blockPos, false);
						world.levelEvent(1029, blockPos, 0);
					}
					else {
						world.setBlock(blockPos, landingState, 2);
						world.levelEvent(1030, blockPos, 0);
					}
				}
				else {
					world.levelEvent(1030, blockPos, 0);
				}
			});
			info.cancel();
		}
	}
	
	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	public void be_updateOutput(CallbackInfo info) {
		RecipeManager recipeManager = this.player.level.getRecipeManager();
		be_recipes = recipeManager.getRecipesFor(AnvilRecipe.TYPE, inputSlots, player.level);
		if (be_recipes.size() > 0) {
			int anvilLevel = this.anvilLevel.get();
			be_recipes = be_recipes.stream().filter(recipe -> anvilLevel >= recipe.getAnvilLevel()).collect(Collectors.toList());
			if (be_recipes.size() > 0) {
				if (be_currentRecipe == null || !be_recipes.contains(be_currentRecipe)) {
					be_currentRecipe = be_recipes.get(0);
				}
				be_updateResult();
				info.cancel();
			}
			else {
				be_currentRecipe = null;
			}
		}
	}
	
	@Inject(method = "setItemName", at = @At("HEAD"), cancellable = true)
	public void be_setNewItemName(String string, CallbackInfo info) {
		if (be_currentRecipe != null) {
			info.cancel();
		}
	}
	
	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) {
			be_previousRecipe();
			return true;
		}
		else if (id == 1) {
			be_nextRecipe();
			return true;
		}
		return super.clickMenuButton(player, id);
	}
	
	private void be_updateResult() {
		if (be_currentRecipe == null) return;
		resultSlots.setItem(0, be_currentRecipe.assemble(inputSlots));
		broadcastChanges();
	}
	
	@Override
	public void be_updateCurrentRecipe(AnvilRecipe recipe) {
		this.be_currentRecipe = recipe;
		be_updateResult();
	}
	
	@Override
	public AnvilRecipe be_getCurrentRecipe() {
		return be_currentRecipe;
	}
	
	@Override
	public List<AnvilRecipe> be_getRecipes() {
		return be_recipes;
	}
}
