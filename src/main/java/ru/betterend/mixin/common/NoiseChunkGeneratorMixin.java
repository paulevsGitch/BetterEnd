package ru.betterend.mixin.common;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import ru.betterend.world.generator.GeneratorOptions;
import ru.betterend.world.generator.TerrainGenerator;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator {
	@Final
	@Shadow
	protected Supplier<ChunkGeneratorSettings> settings;

	public NoiseChunkGeneratorMixin(BiomeSource populationSource, BiomeSource biomeSource,
			StructuresConfig structuresConfig, long worldSeed) {
		super(populationSource, biomeSource, structuresConfig, worldSeed);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/world/biome/source/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("TAIL"))
	private void beOnInit(BiomeSource populationSource, BiomeSource biomeSource, long seed,
			Supplier<ChunkGeneratorSettings> settings, CallbackInfo info) {
		TerrainGenerator.initNoise(seed);
	}

	@Inject(method = "sampleNoiseColumn([DII)V", at = @At("HEAD"), cancellable = true, allow = 2)
	private void beSampleNoiseColumn(double[] buffer, int x, int z, CallbackInfo info) {
		if (GeneratorOptions.useNewGenerator() && settings.get().equals(ChunkGeneratorSettings.END)) {
			// System.out.println(TerrainGenerator.canGenerate(x, z));
			// if (TerrainGenerator.canGenerate(x, z)) {
			TerrainGenerator.fillTerrainDensity(buffer, x, z, getBiomeSource());
			info.cancel();
			// }
		}
	}
}
