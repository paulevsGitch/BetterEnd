package ru.betterend.world.generator;

import java.util.Random;

import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class TerrainGenerator {
	private static final double SCALE_XZ = 8.0;
	private static final double SCALE_Y = 4.0;
	
	private static IslandLayer largeIslands;
	private static IslandLayer mediumIslands;
	private static IslandLayer smallIslands;
	private static OpenSimplexNoise noise1;
	private static OpenSimplexNoise noise2;
	
	private static OpenSimplexNoise continentalTop;
	private static OpenSimplexNoise continentalBottom;
	private static OpenSimplexNoise continentalSeparator;
	
	public static void initNoise(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), 300, 200, 63, 0);
		mediumIslands = new IslandLayer(random.nextInt(), 150, 100, 63, 16);
		smallIslands = new IslandLayer(random.nextInt(), 60, 50, 63, 32);
		noise1 = new OpenSimplexNoise(random.nextInt());
		noise2 = new OpenSimplexNoise(random.nextInt());
		continentalTop = new OpenSimplexNoise(random.nextInt());
		continentalBottom = new OpenSimplexNoise(random.nextInt());
		continentalSeparator = new OpenSimplexNoise(random.nextInt());
	}
	
	public static void fillTerrainDensity(double[] buffer, int x, int z) {
		largeIslands.clearCache();
		
		double px = (double) x * SCALE_XZ;
		double pz = (double) z * SCALE_XZ;
		
		largeIslands.updatePositions(px, pz);
		
		for (int y = 0; y < buffer.length; y++) {
			double py = (double) y * SCALE_Y;
			//float dist = getContinental(px, py, pz);
			//dist = dist > 1 ? dist : MHelper.max(dist, largeIslands.getDensity(px, py, pz));
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.04;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.02;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.01;
			}
			buffer[y] = dist;
		}
	}
	
	private static float getContinental(double px, double py, double pz) {
		float gradient = ((float) py - 50F) * 0.01F;
		float top = (float) continentalTop.eval(px * 0.04, py * 0.04, pz * 0.04) * 0.1F - gradient;
		float bottom = (float) continentalBottom.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.5F + gradient;
		float separate = (float) Math.abs(continentalSeparator.eval(px * 0.002, py * 0.002, pz * 0.002));
		return MHelper.min(top, bottom) * (separate * separate) + (float) noise2.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.06F;
	}
}
