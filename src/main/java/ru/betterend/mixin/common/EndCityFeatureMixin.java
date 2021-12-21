package ru.betterend.mixin.common;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.EndCityFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Optional;
import java.util.Random;

@Mixin(EndCityFeature.class)
public class EndCityFeatureMixin {
	@Inject(method = "pieceGeneratorSupplier", at = @At("HEAD"), cancellable = true)
	private static void be_isFeatureChunk(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context, CallbackInfoReturnable<Optional<PieceGenerator<NoneFeatureConfiguration>>> info) {
		final ChunkPos pos = context.chunkPos();
		final ChunkGenerator chunkGenerator = context.chunkGenerator();
		final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();
		Random chunkRandom = new WorldgenRandom(new XoroshiroRandomSource(pos.x, pos.z));
		
		if (GeneratorOptions.useNewGenerator()) {
			int chance = GeneratorOptions.getEndCityFailChance();
			if (chance == 0 || chunkRandom.nextInt(chance) == 0) {
				if (!(getYPositionForFeature(pos, chunkGenerator, levelHeightAccessor) >= 60)){
					info.cancel();
					info.setReturnValue(Optional.empty());
				}
			}
			else {
				info.setReturnValue(Optional.empty());
				info.cancel();
			}
		}
	}
	
	@Shadow
	private static int getYPositionForFeature(ChunkPos pos, ChunkGenerator chunkGenerator, LevelHeightAccessor levelHeightAccessor) {
		return 0;
	}
}
