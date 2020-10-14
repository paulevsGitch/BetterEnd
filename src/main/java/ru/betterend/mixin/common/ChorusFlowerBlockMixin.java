package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import ru.betterend.registry.BlockRegistry;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void canPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (world.getBlockState(pos.down()).isOf(BlockRegistry.CHORUS_NYLIUM)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
