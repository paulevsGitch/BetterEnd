package ru.betterend.mixin.common;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.world.generator.TerrainGenerator;

import java.util.function.Supplier;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator {
	public NoiseBasedChunkGeneratorMixin(BiomeSource populationSource, BiomeSource biomeSource, StructureSettings structuresConfig, long worldSeed) {
		super(populationSource, biomeSource, structuresConfig, worldSeed);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("TAIL"))
	private void be_onInit(Registry registry, BiomeSource biomeSource, BiomeSource biomeSource2, long seed, Supplier supplier, CallbackInfo ci) {
		TerrainGenerator.initNoise(seed);
	}
}
