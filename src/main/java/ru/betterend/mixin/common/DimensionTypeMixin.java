package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import ru.betterend.world.generator.BetterEndBiomeSource;

@Mixin(DimensionType.class)
public class DimensionTypeMixin
{
	@Inject(method = "createEndGenerator", at = @At("HEAD"), cancellable = true)
	private static void replaceGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed, CallbackInfoReturnable<ChunkGenerator> info)
	{
		info.setReturnValue(new NoiseChunkGenerator(new BetterEndBiomeSource(biomeRegistry, seed), seed, () -> {
			return (ChunkGeneratorSettings) chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.END);
		}));
		info.cancel();
	}
}