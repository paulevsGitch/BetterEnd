package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndSpikeFeature.class)
public class EndSpikeFeatureMixin {
	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void beSpikeGenerate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, EndSpikeFeatureConfig endSpikeFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPillars()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
}
