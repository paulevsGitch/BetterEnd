package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ChorusPlantFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

@Mixin(ChorusPlantFeature.class)
public class ChorusPlantFeatureMixin {
	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void onGenerate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (structureWorldAccess.isAir(blockPos) && structureWorldAccess.getBlockState(blockPos.down()).isOf(BlockRegistry.CHORUS_NYLIUM)) {
			ChorusFlowerBlock.generate(structureWorldAccess, blockPos, random, MHelper.randRange(8, 16, random));
			BlockState bottom = structureWorldAccess.getBlockState(blockPos);
			if (bottom.isOf(Blocks.CHORUS_PLANT)) {
				BlocksHelper.setWithoutUpdate(structureWorldAccess, blockPos, bottom.with(BlocksHelper.ROOTS, true));
			}
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
