package ru.betterend.world.generator;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.util.math.MathHelper;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;

public class TerrainGenerator {
	private static final ReentrantLock LOCKER = new ReentrantLock();
	private static final double SCALE_XZ = 8.0;
	private static final double SCALE_Y = 4.0;
	private static final int CENTER = MHelper.floor(500 / SCALE_XZ);
	
	private static IslandLayer largeIslands;
	private static IslandLayer mediumIslands;
	private static IslandLayer smallIslands;
	private static OpenSimplexNoise noise1;
	private static OpenSimplexNoise noise2;
	
	public static boolean canGenerate(int x, int z) {
		return GeneratorOptions.noRingVoid() || (long) x + (long) z > CENTER;
	}
	
	public static void initNoise(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), GeneratorOptions.bigOptions);
		mediumIslands = new IslandLayer(random.nextInt(), GeneratorOptions.mediumOptions);
		smallIslands = new IslandLayer(random.nextInt(), GeneratorOptions.smallOptions);
		noise1 = new OpenSimplexNoise(random.nextInt());
		noise2 = new OpenSimplexNoise(random.nextInt());
	}
	
	public static void fillTerrainDensity(double[] buffer, int x, int z) {
		LOCKER.lock();
		
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		double distortion1 = noise1.eval(x * 0.1, z * 0.1) * 20 + noise2.eval(x * 0.2, z * 0.2) * 10 + noise1.eval(x * 0.4, z * 0.4) * 5;
		double distortion2 = noise2.eval(x * 0.1, z * 0.1) * 20 + noise1.eval(x * 0.2, z * 0.2) * 10 + noise2.eval(x * 0.4, z * 0.4) * 5;
		double px = (double) x * SCALE_XZ + distortion1;
		double pz = (double) z * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < buffer.length; y++) {
			double py = (double) y * SCALE_Y;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			buffer[y] = dist;
		}
		
		LOCKER.unlock();
	}
	
	/**
	 * Check if this is land
	 * @param x - biome pos x
	 * @param z - biome pos z
	 */
	public static boolean isLand(int x, int z) {
		LOCKER.lock();
		
		double px = (x >> 1) + 0.5;
		double pz = (z >> 1) + 0.5;
		
		double distortion1 = noise1.eval(px * 0.1, pz * 0.1) * 20 + noise2.eval(px * 0.2, pz * 0.2) * 10 + noise1.eval(px * 0.4, pz * 0.4) * 5;
		double distortion2 = noise2.eval(px * 0.1, pz * 0.1) * 20 + noise1.eval(px * 0.2, pz * 0.2) * 10 + noise2.eval(px * 0.4, pz * 0.4) * 5;
		px = px * SCALE_XZ + distortion1;
		pz = pz * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < 32; y++) {
			double py = (double) y * SCALE_Y;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			if (dist > -0.01) {
				LOCKER.unlock();
				return true;
			}
		}
		
		LOCKER.unlock();
		return false;
	}
	
	/**
	 * Get something like height
	 * @param x - block pos x
	 * @param z - block pos z
	 */
	public static int getHeight(int x, int z) {
		LOCKER.lock();
		
		double px = (double) x / 8.0;
		double pz = (double) z / 8.0;
		
		double distortion1 = noise1.eval(px * 0.1, pz * 0.1) * 20 + noise2.eval(px * 0.2, pz * 0.2) * 10 + noise1.eval(px * 0.4, pz * 0.4) * 5;
		double distortion2 = noise2.eval(px * 0.1, pz * 0.1) * 20 + noise1.eval(px * 0.2, pz * 0.2) * 10 + noise2.eval(px * 0.4, pz * 0.4) * 5;
		px = (double) x * SCALE_XZ + distortion1;
		pz = (double) z * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 32; y >= 0; y--) {
			double py = (double) y * SCALE_Y;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			if (dist > 0) {
				LOCKER.unlock();
				return MathHelper.floor(MathHelper.clamp(y + dist, y, y + 1) * SCALE_Y);
			}
		}
		
		LOCKER.unlock();
		return 0;
	}
}
