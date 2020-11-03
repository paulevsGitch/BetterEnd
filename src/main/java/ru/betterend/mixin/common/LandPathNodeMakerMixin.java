package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import ru.betterend.registry.EndBlocks;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin {
	@Inject(method = "getCommonNodeType", at = @At(value = "HEAD"), cancellable = true)
	private static void beModifyPathNodes(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> info) {
		BlockState blockState = blockView.getBlockState(blockPos);
		if (blockState.isOf(EndBlocks.NEEDLEGRASS) || blockState.isOf(EndBlocks.MURKWEED)) {
			beCactusDamage(info);
		}
	}
	
	private static void beCactusDamage(CallbackInfoReturnable<PathNodeType> info) {
		info.setReturnValue(PathNodeType.DAMAGE_CACTUS);
		info.cancel();
	}
}
