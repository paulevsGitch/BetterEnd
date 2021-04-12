package ru.betterend.mixin.common;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.EndCityFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndCityFeature.class)
public class EndCityFeatureMixin {
	@Inject(method = "shouldStartAt", at = @At("HEAD"), cancellable = true)
	private void be_shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, WorldgenRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration defaultFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (GeneratorOptions.useNewGenerator()) {
			int chance = GeneratorOptions.getEndCityFailChance();
			if (chance == 0) {
				info.setReturnValue(getGenerationHeight(i, j, chunkGenerator) >= 60);
				info.cancel();
			}
			else if (chunkRandom.nextInt(chance) == 0){
				info.setReturnValue(getGenerationHeight(i, j, chunkGenerator) >= 60);
				info.cancel();
			}
			else {
				info.setReturnValue(false);
				info.cancel();
			}
		}
	}
	
	@Shadow
	private static int getGenerationHeight(int chunkX, int chunkZ, ChunkGenerator chunkGenerator) {
		return 0;
	}
}
