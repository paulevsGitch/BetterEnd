package ru.betterend.world.generator;

import java.util.Random;

import ru.betterend.world.biome.EndBiome;

public class BiomeChunk
{
	protected static final int WIDTH = 16;
	private static final int SM_WIDTH = WIDTH >> 1;
	private static final int MASK_A = SM_WIDTH - 1;
	private static final int MASK_C = WIDTH - 1;
	
	private final EndBiome[][] biomes;
	
	public BiomeChunk(BiomeMap map, Random random, BiomePicker picker)
	{
		EndBiome[][] PreBio = new EndBiome[SM_WIDTH][SM_WIDTH];
		biomes = new EndBiome[WIDTH][WIDTH];
		
		for (int x = 0; x < SM_WIDTH; x++)
			for (int z = 0; z < SM_WIDTH; z++)
				PreBio[x][z] = picker.getBiome(random);
	
		for (int x = 0; x < WIDTH; x++)
			for (int z = 0; z < WIDTH; z++)
				biomes[x][z] = PreBio[offsetXZ(x, random)][offsetXZ(z, random)].getSubBiome(random);
	}

	public EndBiome getBiome(int x, int z)
	{
		return biomes[x & MASK_C][z & MASK_C];
	}
	
	private int offsetXZ(int x, Random random)
	{
		return ((x + random.nextInt(2)) >> 1) & MASK_A;
	}
}
