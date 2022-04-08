package ru.betterend.mixin.common;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.interfaces.TargetChecker;
import ru.betterend.world.generator.TerrainGenerator;

import java.util.List;
import java.util.Objects;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin implements TargetChecker {
	private boolean be_isEndGenerator;

	private static boolean be_is(NoiseGeneratorSettings gen, ResourceKey<NoiseGeneratorSettings> resourceKey) {
		return Objects.equals(gen, BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(resourceKey));
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onNoiseChunkInit(int i, int j, int k, NoiseRouter noiseRouter, int l, int m, DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, CallbackInfo ci) {
		//be_isEndGenerator = noiseGeneratorSettings.is(NoiseGeneratorSettings.END);
		be_isEndGenerator = be_is(noiseGeneratorSettings, NoiseGeneratorSettings.END);
	}
	
	@Override
	public boolean isTarget() {
		return be_isEndGenerator;
	}

	@Final
	@Shadow(aliases = "this$0")
	private NoiseChunk this$0;

	@Shadow @Final private List<NoiseChunk.NoiseInterpolator> interpolators;

	@Inject(method = "fillSlice", at = @At("HEAD"), cancellable = true)
	private void be_fillSlice(boolean primarySlice, int x, CallbackInfo info) {
		if (!TargetChecker.class.cast(this$0).isTarget()) {
			return;
		}

		info.cancel();

		NoiseChunkAccessor accessor = NoiseChunkAccessor.class.cast(this$0);
		NoiseSettings noiseSettings = accessor.bnv_getNoiseSettings();

		final int sizeY = noiseSettings.getCellHeight();
		final int sizeXZ = noiseSettings.getCellWidth();
		//final int cellsY = accessor.bnv_getCellCountY() + 1;
		final int cellSizeXZ = accessor.bnv_getCellCountXZ() + 1;
		final int firstCellZ = accessor.bnv_getFirstCellZ();
		//final int cellNoiseMinY = accessor.bnv_getCellNoiseMinY();

		x *= sizeXZ;

		for (int cellXZ = 0; cellXZ < cellSizeXZ; ++cellXZ) {
			int z = (firstCellZ + cellXZ) * sizeXZ;
			for (NoiseChunk.NoiseInterpolator noiseInterpolator : this.interpolators) {
				if (noiseInterpolator instanceof NoiseInterpolatorMixin ni) {
					//TODO: 1.18.2 Check this implementation
					final double[] ds = (primarySlice ? ni.be_getSlice0() : ni.be_getSlice1())[cellXZ];
					TerrainGenerator.fillTerrainDensity(ds, x, z, sizeXZ, sizeY);
				}
			}
		}
	}
}
