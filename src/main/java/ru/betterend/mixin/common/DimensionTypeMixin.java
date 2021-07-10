package ru.betterend.mixin.common;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.world.generator.BetterEndBiomeSource;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(value = DimensionType.class, priority = 100)
public class DimensionTypeMixin {
	@Inject(method = "defaultEndGenerator", at = @At("HEAD"), cancellable = true)
	private static void be_replaceGenerator(Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> chunkGeneratorSettingsRegistry, long seed, CallbackInfoReturnable<ChunkGenerator> info) {
		info.setReturnValue(new NoiseBasedChunkGenerator(new BetterEndBiomeSource(biomeRegistry, seed), seed, () -> chunkGeneratorSettingsRegistry.getOrThrow(NoiseGeneratorSettings.END)));
	}
	
	@Inject(method = "createDragonFight", at = @At("HEAD"), cancellable = true)
	private void be_hasEnderDragonFight(CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasDragonFights()) {
			info.setReturnValue(false);
		}
	}
}