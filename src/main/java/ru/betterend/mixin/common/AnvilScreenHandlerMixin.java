package ru.betterend.mixin.common;

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
import net.minecraft.world.World;
import ru.betterend.recipe.builders.AnvilSmithingRecipe;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

	private final World world = this.player.world;
	private final RecipeManager recipeManager = this.world.getRecipeManager();
	private AnvilSmithingRecipe currentRecipe;
	
	public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}
	
	@Shadow
	public abstract void updateResult();
	
	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
		if (currentRecipe != null) {
			ItemStack output = this.currentRecipe.craft(input, player);
			if (!output.isEmpty()) {
				info.setReturnValue(true);
				info.cancel();
			}
		}
	}
	
	@Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
	protected void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (currentRecipe != null) {
			this.input.getStack(1).decrement(1);
			this.updateResult();
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
			info.cancel();
		}
	}
	
	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	public void updateOutput(CallbackInfo info) {
		this.currentRecipe = this.recipeManager.getFirstMatch(AnvilSmithingRecipe.TYPE, input, world).orElse(null);
		if (currentRecipe != null) {
			this.output.setStack(0, currentRecipe.craft(input));
			this.sendContentUpdates();
			info.cancel();
		}
	}
	
	@Inject(method = "setNewItemName", at = @At("HEAD"), cancellable = true)
	public void setNewItemName(String string, CallbackInfo info) {
		if (currentRecipe != null) {
			info.cancel();
		}
	}
}
