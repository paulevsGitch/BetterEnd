package ru.betterend.mixin.common;

import java.util.Arrays;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
	@Final
	@Shadow
	protected Supplier<ChunkGeneratorSettings> settings;
	
	@Final
	@Shadow
	private int noiseSizeY;
	
	/*@Inject(method = "sampleNoise", at = @At("HEAD"), cancellable = true)
	private void beSampleEndNoise(int x, int y, int z, double horizontalScale, double verticalScale, double horizontalStretch, double verticalStretch, CallbackInfoReturnable<Double> info) {
		if (settings.get().equals(ChunkGeneratorSettings.END)) {
			double value = ((x + y + z) & 1) == 0 ? 1 : -1;
			info.setReturnValue(value);
			info.cancel();
		}
	}*/
	
	@Inject(method = "sampleNoiseColumn([DII)V", at = @At("HEAD"), cancellable = true, allow = 2)
	private void beSampleNoiseColumn(double[] buffer, int x, int z, CallbackInfo info) {
		if (settings.get().equals(ChunkGeneratorSettings.END)) {
			//System.out.println("Replace!");
			//Arrays.fill(buffer, -100);
			//Arrays.fill(buffer, 0, buffer.length >> 1, 100);
			//info.cancel();
			
			float center = buffer.length * 0.5F;
			/*float sin = (float) (Math.sin(x * 0.1) * Math.sin(z * 0.1)) * center * 0.5F;
			int min = (int) (center - sin);
			int max = (int) (center + sin);
			if (max > min) {
				Arrays.fill(buffer, min, max, 1);
			}*/
			
			float sin = (float) (Math.sin(x * 0.5) * Math.sin(z * 0.5)) * center * 0.5F;
			for (int y = 0; y < buffer.length; y++) {
				float value = (y - center);
				buffer[y] = sin * 0.01F - Math.abs(value) * 0.01F;
			}
			
			info.cancel();
		}
	}
}
