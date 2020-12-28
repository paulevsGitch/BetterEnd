package ru.betterend.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.util.math.BlockPos;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.primitive.SDFCapedCone;

public class IslandLayer {
	private static final List<BlockPos> POSITIONS = new ArrayList<BlockPos>(9);
	private static final Random RANDOM = new Random();
	
	private final Map<BlockPos, SDF> islands = Maps.newHashMap();
	private final double distance;
	private final int seed;
	private final int minY;
	private final int maxY;
	
	public IslandLayer(int seed, double distance, int minY, int maxY) {
		this.distance = distance;
		this.seed = seed;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	private int getSeed(int x, int y, int z) {
		int h = seed + x * 374761393 + y * 668265263 + z;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	private void updatePositions(double x, double y, double z) {
		POSITIONS.clear();
		
		int ix = MHelper.floor(x / distance);
		int iy = MHelper.floor(y / distance);
		int iz = MHelper.floor(z / distance);
		
		for (int pox = -1; pox < 2; pox++) {
			int px = pox + ix;
			for (int poy = -1; poy < 2; poy++) {
				int py = poy + iy;
				for (int poz = -1; poz < 2; poz++) {
					int pz = poz + iz;
					RANDOM.setSeed(getSeed(px, py, pz));
					double posX = (px + RANDOM.nextFloat()) * distance;
					double posY = (py + RANDOM.nextFloat()) * distance;
					double posZ = (pz + RANDOM.nextFloat()) * distance;
					POSITIONS.add(new BlockPos(posX, posY, posZ));
				}
			}
		}
	}
	
	private SDF getIsland(BlockPos pos) {
		SDF island = islands.get(pos);
		if (island == null) {
			island = new SDFCapedCone().setHeight(10).setRadius1(0).setRadius2(30);
			islands.put(pos, island);
		}
		return island;
	}
	
	private float getRelativeDistance(SDF sdf, BlockPos center, double px, double py, double pz) {
		float x = (float) (px - center.getX());
		float y = (float) (py - center.getY());
		float z = (float) (pz - center.getZ());
		return sdf.getDistance(x, y, z);
	}
	
	private float calculateSDF(double x, double y, double z) {
		float distance = 10;
		for (BlockPos pos: POSITIONS) {
			if (pos.getY() > minY && pos.getY() < maxY) {
				SDF island = getIsland(pos);
				float dist = getRelativeDistance(island, pos, x, y, z);
				distance = MHelper.min(distance, dist);
			}
		}
		return distance;
	}
	
	public float getDensity(double x, double y, double z) {
		updatePositions(x, y, z);
		return -calculateSDF(x, y, z);
	}
}
