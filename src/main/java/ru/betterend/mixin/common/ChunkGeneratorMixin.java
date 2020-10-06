package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.registry.DefaultBiomeFeatures;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
	/*@Inject(method = "generateFeatures", at = @At("HEAD"))
	private void fixerPass(ChunkRegion region, StructureAccessor accessor, CallbackInfo info) {
		int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		BlockPos start = new BlockPos(chunkX << 4, 16, chunkZ << 4);
		DefaultBiomeFeatures.MOUNTAINS.getFeature()
	}*/
}
