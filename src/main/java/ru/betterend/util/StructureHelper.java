package ru.betterend.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StructureHelper {
	private static final Set<ChunkPos> POSITIONS = new HashSet<ChunkPos>(64);

	private static void collectNearby(ServerWorld world, StructureFeature<?> feature, int chunkX, int chunkZ, int radius, StructureConfig config, long worldSeed, ChunkRandom chunkRandom) {
		int x1 = chunkX - radius;
		int x2 = chunkX + radius;
		int z1 = chunkZ - radius;
		int z2 = chunkZ + radius;

		POSITIONS.clear();
		ChunkGenerator generator = world.getChunkManager().getChunkGenerator();
		for (int x = x1; x <= x2; x += 8) {
			for (int z = z1; z <= z2; z += 8) {
				ChunkPos chunk = feature.getStartChunk(config, worldSeed, chunkRandom, x, z);
				if (world.getBiome(chunk.getStartPos()).getGenerationSettings().hasStructureFeature(feature))
				{
					if (feature.getName().equals("endcity")) {
						if (generator.getHeight((x << 16) | 8, (z << 16) | 8, Heightmap.Type.WORLD_SURFACE_WG) > 60) {
							POSITIONS.add(chunk);
						}
					}
					else {
						POSITIONS.add(chunk);
					}
				}
			}
		}
	}

	private static long sqr(int x) {
		return (long) x * (long) x;
	}

	public static BlockPos getNearestStructure(StructureFeature<?> feature, ServerWorld world, BlockPos pos, int radius) {
		int cx = pos.getX() >> 4;
		int cz = pos.getZ() >> 4;

		StructureConfig config = world.getChunkManager().getChunkGenerator().getStructuresConfig().getForType(feature);
		if (config == null)
			return null;

		collectNearby(world, feature, cx, cz, radius, config, world.getSeed(), new ChunkRandom());
		Iterator<ChunkPos> iterator = POSITIONS.iterator();
		if (iterator.hasNext()) {
			ChunkPos nearest = POSITIONS.iterator().next();
			long d = sqr(nearest.x - cx) + sqr(nearest.z - cz);
			while (iterator.hasNext()) {
				ChunkPos n = iterator.next();
				long d2 = sqr(n.x - cx) + sqr(n.z - cz);
				if (d2 < d) {
					d = d2;
					nearest = n;
				}
			}
			return nearest.getStartPos();
		}
		return null;
	}
}
