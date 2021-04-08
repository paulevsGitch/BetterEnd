package ru.betterend.world.surface;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class DoubleBlockSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(4141);
	private TernarySurfaceConfig config1;
	private TernarySurfaceConfig config2;

	private DoubleBlockSurfaceBuilder() {
		super(TernarySurfaceConfig.CODEC);
	}

	public DoubleBlockSurfaceBuilder setBlock1(Block block) {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		config1 = new TernarySurfaceConfig(block.defaultBlockState(), stone, stone);
		return this;
	}

	public DoubleBlockSurfaceBuilder setBlock2(Block block) {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		config2 = new TernarySurfaceConfig(block.defaultBlockState(), stone, stone);
		return this;
	}

	@Override
	public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise,
			BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed,
			TernarySurfaceConfig surfaceBlocks) {
		noise = NOISE.eval(x * 0.1, z * 0.1) + MHelper.randRange(-0.4, 0.4, random);
		SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel,
				seed, noise > 0 ? config1 : config2);
	}

	public static DoubleBlockSurfaceBuilder register(String name) {
		return Registry.register(Registry.SURFACE_BUILDER, name, new DoubleBlockSurfaceBuilder());
	}

	public ConfiguredSurfaceBuilder<TernarySurfaceConfig> configured() {
		BlockState stone = Blocks.END_STONE.defaultBlockState();
		return this.withConfig(new TernarySurfaceConfig(config1.getTopMaterial(), stone, stone));
	}
}