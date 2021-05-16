package ru.betterend.world.structures.features;

import java.util.Random;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class FeatureBaseStructure extends StructureFeature<NoneFeatureConfiguration> {
	protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
	
	public FeatureBaseStructure() {
		super(NoneFeatureConfiguration.CODEC);
	}
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, WorldgenRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfig) {
		return getGenerationHeight(chunkX, chunkZ, chunkGenerator) >= 20;
	}

	private static int getGenerationHeight(int chunkX, int chunkZ, ChunkGenerator chunkGenerator) {
		Random random = new Random((long) (chunkX + chunkZ * 10387313));
		Rotation blockRotation = Rotation.getRandom(random);
		int i = 5;
		int j = 5;
		if (blockRotation == Rotation.CLOCKWISE_90) {
			i = -5;
		} else if (blockRotation == Rotation.CLOCKWISE_180) {
			i = -5;
			j = -5;
		} else if (blockRotation == Rotation.COUNTERCLOCKWISE_90) {
			j = -5;
		}

		int k = (chunkX << 4) + 7;
		int l = (chunkZ << 4) + 7;
		int m = chunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG);
		int n = chunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG);
		int o = chunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG);
		int p = chunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG);
		return Math.min(Math.min(m, n), Math.min(o, p));
	}
}
