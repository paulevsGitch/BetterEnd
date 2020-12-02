package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.BlockRenderView;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	private static final int POISON_COLOR = MHelper.color(92, 160, 78);
	
	@Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
	private static void beGetWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		int x1 = pos.getX() - 1;
		int y1 = pos.getY() - 1;
		int z1 = pos.getZ() - 1;
		int x2 = pos.getX() + 2;
		int y2 = pos.getY() + 2;
		int z2 = pos.getZ() + 2;
		Mutable mut = new Mutable();
		for (int x = x1; x < x2; x++) {
			mut.setX(x);
			for (int y = y1; y < y2; y++) {
				mut.setY(y);
				for (int z = z1; z < z2; z++) {
					mut.setZ(z);
					if (world.getBlockState(mut).isOf(EndBlocks.BRIMSTONE)) {
						info.setReturnValue(POISON_COLOR);
						info.cancel();
						return;
					}
				}
			}
		}
	}
}
