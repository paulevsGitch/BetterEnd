package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {
	@Shadow
	@Final
	private ScreenHandlerContext context;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void be_canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (context.run((world, pos) -> {
			return world.getBlockState(pos).getBlock() instanceof CraftingTableBlock;
		}, true)) {
			info.setReturnValue(true);
		}
	}
}
