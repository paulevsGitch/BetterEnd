package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ConnectingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ChorusPlantFeature.class)
public class ChorusPlantFeatureMixin {
	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void be_onGenerate(WorldGenLevel structureWorldAccess, ChunkGenerator chunkGenerator, Random random,
			BlockPos blockPos, NoneFeatureConfiguration defaultFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (structureWorldAccess.isAir(blockPos)
				&& structureWorldAccess.getBlockState(blockPos.below()).is(EndBlocks.CHORUS_NYLIUM)) {
			ChorusFlowerBlock.generate(structureWorldAccess, blockPos, random, MHelper.randRange(8, 16, random));
			BlockState bottom = structureWorldAccess.getBlockState(blockPos);
			if (bottom.is(Blocks.CHORUS_PLANT)) {
				if ((GeneratorOptions.changeChorusPlant())) {
					BlocksHelper.setWithoutUpdate(structureWorldAccess, blockPos,
							bottom.with(BlocksHelper.ROOTS, true).with(ConnectingBlock.DOWN, true));
				} else {
					BlocksHelper.setWithoutUpdate(structureWorldAccess, blockPos,
							bottom.with(ConnectingBlock.DOWN, true));
				}
			}
			info.setReturnValue(true);
		}
	}
}
