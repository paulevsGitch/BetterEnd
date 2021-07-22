package ru.betterend.world.surface;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.Random;

public class UmbraSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);
	
	public UmbraSurfaceBuilder() {
		super(SurfaceBuilderBaseConfiguration.CODEC);
	}
	
	@Override
	public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int seed, long n, SurfaceBuilderBaseConfiguration surfaceBlocks) {
		int depth = (int) (NOISE.eval(x * 0.1, z * 0.1) * 20 + NOISE.eval(x * 0.5, z * 0.5) * 10 + 60);
		float grass = ((float) NOISE.eval(x * 0.03, z * 0.03) + (float) NOISE.eval(x * 0.1, z * 0.1) * 0.6F + random.nextFloat() * 0.2F) - 0.05F;
		SurfaceBuilderBaseConfiguration config = null;
		if (grass > 0.3F) {
			config = SurfaceBuilders.PALLIDIUM_SURFACE_CONFIG;
		}
		else if (grass > 0.1F) {
			config = SurfaceBuilders.PALLIDIUM_T1_SURFACE_CONFIG;
		}
		else if (grass > -0.1) {
			config = SurfaceBuilders.PALLIDIUM_T2_SURFACE_CONFIG;
		}
		else if (grass > -0.3F) {
			config = SurfaceBuilders.PALLIDIUM_T3_SURFACE_CONFIG;
		}
		else {
			config = SurfaceBuilders.UMBRA_SURFACE_CONFIG;
		}
		SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, height, noise + depth, defaultBlock, defaultFluid, seaLevel, seed, n, config);
	}
}