package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(value = DimensionType.class, priority = 100)
public class DimensionTypeMixin {
	@Inject(method = "createEndGenerator", at = @At("HEAD"), cancellable = true)
	private static void be_replaceGenerator(Registry<Biome> biomeRegistry,
			Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed,
			CallbackInfoReturnable<ChunkGenerator> info) {
		info.setReturnValue(new NoiseChunkGenerator(new BetterEndBiomeSource(biomeRegistry, seed), seed, () -> {
			return (ChunkGeneratorSettings) chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.END);
		}));
		info.cancel();
	}

	@Inject(method = "hasEnderDragonFight", at = @At("HEAD"), cancellable = true)
	private void be_hasEnderDragonFight(CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasDragonFights()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
}