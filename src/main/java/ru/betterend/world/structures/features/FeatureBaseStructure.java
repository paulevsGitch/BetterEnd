package ru.betterend.world.structures.features;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public abstract class FeatureBaseStructure extends StructureFeature<DefaultFeatureConfig> {
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	
	public FeatureBaseStructure() {
		super(DefaultFeatureConfig.CODEC);
	}
	
	protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig featureConfig) {
		return getGenerationHeight(chunkX, chunkZ, chunkGenerator) >= 20;
	}

	private static int getGenerationHeight(int chunkX, int chunkZ, ChunkGenerator chunkGenerator) {
		Random random = new Random((long) (chunkX + chunkZ * 10387313));
		BlockRotation blockRotation = BlockRotation.random(random);
		int i = 5;
		int j = 5;
		if (blockRotation == BlockRotation.CLOCKWISE_90) {
			i = -5;
		} else if (blockRotation == BlockRotation.CLOCKWISE_180) {
			i = -5;
			j = -5;
		} else if (blockRotation == BlockRotation.COUNTERCLOCKWISE_90) {
			j = -5;
		}

		int k = (chunkX << 4) + 7;
		int l = (chunkZ << 4) + 7;
		int m = chunkGenerator.getHeightInGround(k, l, Heightmap.Type.WORLD_SURFACE_WG);
		int n = chunkGenerator.getHeightInGround(k, l + j, Heightmap.Type.WORLD_SURFACE_WG);
		int o = chunkGenerator.getHeightInGround(k + i, l, Heightmap.Type.WORLD_SURFACE_WG);
		int p = chunkGenerator.getHeightInGround(k + i, l + j, Heightmap.Type.WORLD_SURFACE_WG);
		return Math.min(Math.min(m, n), Math.min(o, p));
	}
}
