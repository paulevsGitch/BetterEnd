package ru.betterend.world.generator;

import java.util.Random;

public class TerrainGenerator {
	private static final double SCALE_XZ = 8.0;
	private static final double SCALE_Y = 4.0;
	
	private static IslandLayer layer;
	
	public static void initNoise(long seed) {
		Random random = new Random(seed);
		layer = new IslandLayer(random.nextInt(), 100, 40, 90);
	}
	
	public static void fillTerrainDensity(double[] buffer, int x, int z) {
		double px = (double) x * SCALE_XZ;
		double pz = (double) z * SCALE_XZ;
		for (int y = 0; y < buffer.length; y++) {
			double py = (double) y * SCALE_Y;
			float dist = layer.getDensity(px, py, pz);
			buffer[y] = dist;
		}
	}
}
