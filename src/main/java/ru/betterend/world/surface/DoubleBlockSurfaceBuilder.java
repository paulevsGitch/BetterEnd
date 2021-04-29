package ru.betterend.world.surface;

import java.util.Random;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class DoubleBlockSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);
	private SurfaceBuilderBaseConfiguration config1;
	private SurfaceBuilderBaseConfiguration config2;
	
	private DoubleBlockSurfaceBuilder() {
		super(SurfaceBuilderBaseConfiguration.CODEC);
	}
	
	public DoubleBlockSurfaceBuilder setBlock1(Block block) {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		config1 = new SurfaceBuilderBaseConfiguration(block.defaultBlockState(), stone, stone);
		return this;
	}
	
	public DoubleBlockSurfaceBuilder setBlock2(Block block) {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		config2 = new SurfaceBuilderBaseConfiguration(block.defaultBlockState(), stone, stone);
		return this;
	}

	@Override
	public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderBaseConfiguration surfaceBlocks) {
		noise = NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4, 0.4, random);
		SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, seed, noise > 0 ? config1 : config2);
	}
	
	public static DoubleBlockSurfaceBuilder register(String name) {
		return Registry.register(Registry.SURFACE_BUILDER, name, new DoubleBlockSurfaceBuilder());
	}
	
	public ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> configured() {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		return this.configured(new SurfaceBuilderBaseConfiguration(config1.getTopMaterial(), stone, stone));
	}
}