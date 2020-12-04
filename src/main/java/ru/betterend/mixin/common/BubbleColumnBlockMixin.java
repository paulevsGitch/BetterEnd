package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import ru.betterend.registry.EndBlocks;

@Mixin(value = BubbleColumnBlock.class, priority = 100)
public abstract class BubbleColumnBlockMixin extends Block {
	public BubbleColumnBlockMixin(Settings settings) {
		super(settings);
	}
	
	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void beCanPlace(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		BlockState blockState = world.getBlockState(pos.down());
		if (blockState.isOf(EndBlocks.HYDROTHERMAL_VENT)) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "calculateDrag", at = @At("RETURN"), cancellable = true)
	private static void beCalculateDrag(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (info.getReturnValue()) {
			BlockState state = world.getBlockState(pos.down());
			if (state.isOf(EndBlocks.HYDROTHERMAL_VENT)) {
				info.setReturnValue(false);
				info.cancel();
			}
		}
	}
}
