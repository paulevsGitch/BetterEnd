package ru.betterend.mixin.common;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import net.minecraft.world.level.block.AnvilBlock;
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

@Mixin(AnvilMenu.class)
public abstract class AnvilScreenHandlerMixin extends ItemCombinerMenu implements AnvilScreenHandlerExtended {

	private List<AnvilRecipe> be_recipes = Collections.emptyList();
	private AnvilRecipe be_currentRecipe;
	private DataSlot anvilLevel;

	public AnvilScreenHandlerMixin(int syncId, Inventory playerInventory) {
		super(MenuType.ANVIL, syncId, playerInventory, ContainerLevelAccess.NULL);
	}

	@Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
			at = @At("TAIL"))
	public void be_initAnvilLevel(int syncId, Inventory inventory, ContainerLevelAccess context, CallbackInfo info) {
		if (context != ContainerLevelAccess.NULL) {
			int anvLevel = context.evaluate((world, blockPos) -> {
				Block anvilBlock = world.getBlockState(blockPos).getBlock();
				if (anvilBlock instanceof EndAnvilBlock) {
					return ((EndAnvilBlock) anvilBlock).getCraftingLevel();
				}
				return 1;
			}, 1);
			DataSlot anvilLevel = DataSlot.standalone();
			anvilLevel.set(anvLevel);
			this.anvilLevel = addDataSlot(anvilLevel);
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
	protected void be_onTakeOutput(Player player, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (be_currentRecipe != null) {
			this.inputSlots.getItem(0).shrink(be_currentRecipe.getInputCount());
			stack = be_currentRecipe.craft(inputSlots, player);
			this.slotsChanged(inputSlots);
			this.access.execute((world, blockPos) -> {
				BlockState anvilState = world.getBlockState(blockPos);
				if (!player.abilities.instabuild && anvilState.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
					BlockState landingState = AnvilBlock.damage(anvilState);
					if (landingState == null) {
						world.removeBlock(blockPos, false);
						world.levelEvent(1029, blockPos, 0);
					} else {
						world.setBlock(blockPos, landingState, 2);
						world.levelEvent(1030, blockPos, 0);
					}
				} else {
					world.levelEvent(1030, blockPos, 0);
				}
			});
			info.setReturnValue(stack);
		}
	}
	
	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	public void be_updateOutput(CallbackInfo info) {
		RecipeManager recipeManager = this.player.level.getRecipeManager();
		be_recipes = recipeManager.getRecipesFor(AnvilRecipe.TYPE, inputSlots, player.level);
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
	
	@Inject(method = "setItemName", at = @At("HEAD"), cancellable = true)
	public void be_setNewItemName(String string, CallbackInfo info) {
		if (be_currentRecipe != null) {
			info.cancel();
		}
	}
	
	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) {
			this.be_previousRecipe();
			return true;
		} else if (id == 1) {
			this.be_nextRecipe();
			return true;
		}
		return super.clickMenuButton(player, id);
	}
	
	private void be_updateResult() {
		if (be_currentRecipe == null) return;
		this.resultSlots.setItem(0, be_currentRecipe.assemble(inputSlots));
		this.broadcastChanges();
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
