package ru.betterend.world.surface;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class SulphuricSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(5123);

	public SulphuricSurfaceBuilder() {
		super(TernarySurfaceConfig.CODEC);
	}

	@Override
	public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise,
			BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed,
			TernarySurfaceConfig surfaceBlocks) {
		double value = NOISE.eval(x * 0.03, z * 0.03) + NOISE.eval(x * 0.1, z * 0.1) * 0.3
				+ MHelper.randRange(-0.1, 0.1, MHelper.RANDOM);
		if (value < -0.6) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid,
					seaLevel, seed, SurfaceBuilders.DEFAULT_END_CONFIG);
		} else if (value < -0.3) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid,
					seaLevel, seed, SurfaceBuilders.FLAVOLITE_CONFIG);
		} else if (value < 0.5) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid,
					seaLevel, seed, SurfaceBuilders.SULFURIC_ROCK_CONFIG);
		} else {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid,
					seaLevel, seed, SurfaceBuilders.BRIMSTONE_CONFIG);
		}
	}

	public static SulphuricSurfaceBuilder register(String name) {
		return Registry.register(Registry.SURFACE_BUILDER, name, new SulphuricSurfaceBuilder());
	}
}