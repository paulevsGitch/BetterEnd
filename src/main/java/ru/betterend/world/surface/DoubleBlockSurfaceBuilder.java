package ru.betterend.world.surface;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class DoubleBlockSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
	public static final DoubleBlockSurfaceBuilder INSTANCE = new DoubleBlockSurfaceBuilder(TernarySurfaceConfig.CODEC);
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);
	private TernarySurfaceConfig config1;
	private TernarySurfaceConfig config2;
	
	public DoubleBlockSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
		super(codec);
	}
	
	public DoubleBlockSurfaceBuilder setConfigUpper(TernarySurfaceConfig config) {
		config1 = config;
		return this;
	}
	
	public DoubleBlockSurfaceBuilder setConfigLower(TernarySurfaceConfig config) {
		config2 = config;
		return this;
	}

	@Override
	public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig surfaceBlocks) {
		noise = NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4, 0.4, random);
		SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, seed, noise > 0 ? config1 : config2);
	}
	
	public static void register() {
		Registry.register(Registry.SURFACE_BUILDER, "double_block_surface_builder", INSTANCE);
	}
}
