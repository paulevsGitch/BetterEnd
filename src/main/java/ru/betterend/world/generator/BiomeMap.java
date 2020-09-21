package ru.betterend.world.generator;

import java.util.HashMap;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkRandom;
import ru.betterend.MHelper;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.world.biome.EndBiome;

public class BiomeMap
{
	private static final HashMap<ChunkPos, BiomeChunk> MAPS = new HashMap<ChunkPos, BiomeChunk>();
	private static final ChunkRandom RANDOM = new ChunkRandom();
	
	private final int size;
	private final int sizeXZ;
	private final int depth;
	private final OpenSimplexNoise noiseX;;
	private final OpenSimplexNoise noiseZ;
	private final BiomePicker picker;
	
	public BiomeMap(long seed, int size, BiomePicker picker)
	{
		RANDOM.setSeed(seed);
		noiseX = new OpenSimplexNoise(RANDOM.nextLong());
		noiseZ = new OpenSimplexNoise(RANDOM.nextLong());
		this.sizeXZ = size;
		depth = (int) Math.ceil(Math.log(size) / Math.log(2)) - 2;
		this.size = 1 << depth;
		this.picker = picker;
	}
	
	public void clearCache()
	{
		if (MAPS.size() > 16)
			MAPS.clear();
	}
	
	private EndBiome getRawBiome(int bx, int bz)
	{
		double x = (double) bx * size / sizeXZ;
		double z = (double) bz * size / sizeXZ;
		double nx = x;
		double nz = z;
		
		double px = bx * 0.2;
		double pz = bz * 0.2;
		
		for (int i = 0; i < depth; i++)
		{
			nx = (x + noiseX.eval(px, pz)) / 2F;
			nz = (z + noiseZ.eval(px, pz)) / 2F;
			
			x = nx;
			z = nz;
			
			px = px / 2 + i;
			pz = pz / 2 + i;
		}
		
		ChunkPos cpos = new ChunkPos(MHelper.floor((double) x / BiomeChunk.WIDTH), MHelper.floor((double) z / BiomeChunk.WIDTH));
		BiomeChunk chunk = MAPS.get(cpos);
		if (chunk == null)
		{
			RANDOM.setTerrainSeed(cpos.x, cpos.z);
			chunk = new BiomeChunk(this, RANDOM, picker);
			MAPS.put(cpos, chunk);
		}
		
		return chunk.getBiome(MHelper.floor(x), MHelper.floor(z));
	}
	
	public EndBiome getBiome(int x, int z)
	{
		EndBiome biome = getRawBiome(x, z);
		
		if (biome.hasEdge() || (biome.hasParentBiome() && biome.getParentBiome().hasEdge()))
		{
			EndBiome search = biome;
			if (biome.hasParentBiome())
				search = biome.getParentBiome();
			int d = (int) Math.ceil(search.getEdgeSize() / 4F) << 2;
			
			boolean edge = !search.isSame(getRawBiome(x + d, z));
			edge = edge || !search.isSame(getRawBiome(x - d, z));
			edge = edge || !search.isSame(getRawBiome(x, z + d));
			edge = edge || !search.isSame(getRawBiome(x, z - d));
			edge = edge || !search.isSame(getRawBiome(x - 1, z - 1));
			edge = edge || !search.isSame(getRawBiome(x - 1, z + 1));
			edge = edge || !search.isSame(getRawBiome(x + 1, z - 1));
			edge = edge || !search.isSame(getRawBiome(x + 1, z + 1));
			
			if (edge)
			{
				biome = search.getEdge();
			}
		}
		
		return biome;
	}
}
