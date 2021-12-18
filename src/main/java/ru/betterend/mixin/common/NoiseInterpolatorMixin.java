package ru.betterend.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.interfaces.TargetChecker;
import ru.betterend.world.generator.TerrainGenerator;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public class NoiseInterpolatorMixin {
	@Final
	@Shadow(aliases = "this$0")
	private NoiseChunk this$0;
	
	@Inject(method = "fillSlice", at = @At("HEAD"), cancellable = true)
	private void be_fillSlice(double[][] data, int x, CallbackInfo info) {
		if (!TargetChecker.class.cast(this$0).isTarget()) {
			return;
		}
		
		info.cancel();
		
		NoiseChunkAccessor accessor = NoiseChunkAccessor.class.cast(this$0);
		NoiseSettings noiseSettings = accessor.bnv_getNoiseSettings();
		
		final int sizeY = noiseSettings.getCellHeight();
		final int sizeXZ = noiseSettings.getCellWidth();
		//final int cellsY = accessor.bnv_getCellCountY() + 1;
		final int cellsXZ = accessor.bnv_getCellCountXZ() + 1;
		final int firstCellZ = accessor.bnv_getFirstCellZ();
		//final int cellNoiseMinY = accessor.bnv_getCellNoiseMinY();
		
		x *= sizeXZ;
		
		for (int cellXZ = 0; cellXZ < cellsXZ; ++cellXZ) {
			int z = (firstCellZ + cellXZ) * sizeXZ;
			TerrainGenerator.fillTerrainDensity(data[cellXZ], x, z, sizeXZ, sizeY);
		}
	}
}
