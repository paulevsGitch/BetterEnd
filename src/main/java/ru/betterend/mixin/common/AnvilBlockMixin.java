package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.basis.EndAnvilBlock;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
	@Inject(method = "getLandingState", at = @At("HEAD"), cancellable = true)
	private static void be_getLandingState(BlockState fallingState, CallbackInfoReturnable<BlockState> info) {
		if (fallingState.getBlock() instanceof EndAnvilBlock) {
			int destruction = fallingState.get(BlockProperties.DESTRUCTION);
			BlockState state = (destruction < 2) ? fallingState.with(BlockProperties.DESTRUCTION, destruction + 1) : null;
			info.setReturnValue(state);
			info.cancel();
		}
	}
}
