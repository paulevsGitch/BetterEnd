package ru.betterend.mixin.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.SpikeFeature.EndSpike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.WorldDataAPI;
import ru.betterend.BetterEnd;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndSpike.class)
public class EndSpikeMixin {
	@Final
	@Shadow
	private int height;

	@Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
	private void be_getSpikeHeight(CallbackInfoReturnable<Integer> info) {
		if (!GeneratorOptions.isDirectSpikeHeight()) {
			int x = getCenterX();
			int z = getCenterZ();
			String pillarID = String.format("%d_%d", x, z);
			CompoundTag pillar = WorldDataAPI.getCompoundTag(BetterEnd.MOD_ID, "pillars");
			int minY = pillar.contains(pillarID) ? pillar.getInt(pillarID) : 65;
			int maxY = minY + height - 54;
			info.setReturnValue(maxY);
		}
	}

	@Shadow
	public int getCenterX() {
		return 0;
	}

	@Shadow
	public int getCenterZ() {
		return 0;
	}
}
