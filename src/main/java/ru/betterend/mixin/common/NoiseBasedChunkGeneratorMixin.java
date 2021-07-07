package ru.betterend.mixin.common;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.world.generator.GeneratorOptions;
import ru.betterend.world.generator.TerrainGenerator;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator {
	@Final
	@Shadow
	protected Supplier<NoiseGeneratorSettings> settings;

	public NoiseBasedChunkGeneratorMixin(BiomeSource populationSource, BiomeSource biomeSource, StructureSettings structuresConfig, long worldSeed) {
		super(populationSource, biomeSource, structuresConfig, worldSeed);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("TAIL"))
	private void beOnInit(BiomeSource populationSource, BiomeSource biomeSource, long seed, Supplier<NoiseGeneratorSettings> settings, CallbackInfo info) {
		TerrainGenerator.initNoise(seed);
	}

	@Inject(method = "fillNoiseColumn([DIIII)V", at = @At("HEAD"), cancellable = true, allow = 2)
	private void be_fillNoiseColumn(double[] buffer, int x, int z, int k, int l, CallbackInfo info) {
		if (GeneratorOptions.useNewGenerator() && settings.get().stable(NoiseGeneratorSettings.END)) {
			TerrainGenerator.fillTerrainDensity(buffer, x, z, getBiomeSource());
			info.cancel();
		}
	}
}
