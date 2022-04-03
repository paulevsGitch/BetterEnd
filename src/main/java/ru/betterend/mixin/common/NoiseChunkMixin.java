package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.interfaces.TargetChecker;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin implements TargetChecker {
	private boolean be_isEndGenerator;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onNoiseChunkInit(int i, int j, int k, NoiseRouter noiseRouter, int l, int m, DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, CallbackInfo ci) {
		be_isEndGenerator = noiseGeneratorSettings.stable(NoiseGeneratorSettings.END);
	}
	
	@Override
	public boolean isTarget() {
		return be_isEndGenerator;
	}
}
