package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndPortalFeature;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndPortalFeature.class)
public class EndPortalFeatureMixin {
	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void bePortalGenerate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPortal()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@ModifyVariable(method = "generate", ordinal = 0, at = @At("HEAD"))
	private BlockPos be_setPosOnGround(BlockPos blockPos, StructureWorldAccess structureWorldAccess) {
		int y = structureWorldAccess.getChunk(blockPos).sampleHeightmap(Type.WORLD_SURFACE, blockPos.getX() & 15, blockPos.getZ() & 15);
		return new BlockPos(blockPos.getX(), y, blockPos.getZ());
	}
}
