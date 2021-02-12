package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.IntProperty;
import ru.betterend.blocks.basis.EndAnvilBlock;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
	@Inject(method = "getLandingState", at = @At("HEAD"), cancellable = true)
	private static void be_getLandingState(BlockState fallingState, CallbackInfoReturnable<BlockState> info) {
		if (fallingState.getBlock() instanceof EndAnvilBlock) {
			IntProperty destructionProperty = ((EndAnvilBlock) fallingState.getBlock()).getDestructionProperty();
			int destruction = fallingState.get(destructionProperty);
			try {
				BlockState state = fallingState.with(destructionProperty, destruction + 1);
				info.setReturnValue(state);
				info.cancel();
			} catch (Exception ex) {
				info.setReturnValue(null);
				info.cancel();
			}
		}
	}
}
