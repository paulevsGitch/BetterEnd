package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.gen.chunk.ChunkGenerator;

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
