package ru.betterend.world.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import ru.bclib.util.MHelper;
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
		SurfaceBuilderBaseConfiguration config = getConfig(x, z, random);
		SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, height, noise + depth, defaultBlock, defaultFluid, seaLevel, seed, n, config);
	}
	
	public static BlockState getSurfaceState(BlockPos pos) {
		return getConfig(pos.getX(), pos.getZ(), MHelper.RANDOM).getTopMaterial();
	}
	
	private static SurfaceBuilderBaseConfiguration getConfig(int x, int z, Random random) {
		float grass = ((float) NOISE.eval(x * 0.03, z * 0.03) + (float) NOISE.eval(x * 0.1, z * 0.1) * 0.6F + random.nextFloat() * 0.2F) - 0.05F;
		if (grass > 0.4F) {
			return SurfaceBuilders.PALLIDIUM_FULL_SURFACE_CONFIG;
		}
		else if (grass > 0.15F) {
			return SurfaceBuilders.PALLIDIUM_HEAVY_SURFACE_CONFIG;
		}
		else if (grass > -0.15) {
			return SurfaceBuilders.PALLIDIUM_THIN_SURFACE_CONFIG;
		}
		else if (grass > -0.4F) {
			return SurfaceBuilders.PALLIDIUM_TINY_SURFACE_CONFIG;
		}
		else {
			return SurfaceBuilders.UMBRA_SURFACE_CONFIG;
		}
	}
}