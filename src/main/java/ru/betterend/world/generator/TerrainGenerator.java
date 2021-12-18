package ru.betterend.world.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.BiomeSource;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;
import ru.betterend.noise.OpenSimplexNoise;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TerrainGenerator {
	private static final Map<Point, TerrainBoolCache> TERRAIN_BOOL_CACHE_MAP = Maps.newHashMap();
	private static final ReentrantLock LOCKER = new ReentrantLock();
	private static final Point POS = new Point();
	private static final double SCALE_XZ = 8.0;
	private static final double SCALE_Y = 4.0;
	private static final float[] COEF;
	private static final Point[] OFFS;
	
	private static IslandLayer largeIslands;
	private static IslandLayer mediumIslands;
	private static IslandLayer smallIslands;
	private static OpenSimplexNoise noise1;
	private static OpenSimplexNoise noise2;
	
	public static void initNoise(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), GeneratorOptions.bigOptions);
		mediumIslands = new IslandLayer(random.nextInt(), GeneratorOptions.mediumOptions);
		smallIslands = new IslandLayer(random.nextInt(), GeneratorOptions.smallOptions);
		noise1 = new OpenSimplexNoise(random.nextInt());
		noise2 = new OpenSimplexNoise(random.nextInt());
		TERRAIN_BOOL_CACHE_MAP.clear();
	}
	
	public static void fillTerrainDensity(double[] buffer, int x, int z, BiomeSource biomeSource) {
		LOCKER.lock();
		
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		double distortion1 = noise1.eval(x * 0.1, z * 0.1) * 20 + noise2.eval(
			x * 0.2,
			z * 0.2
		) * 10 + noise1.eval(x * 0.4, z * 0.4) * 5;
		double distortion2 = noise2.eval(x * 0.1, z * 0.1) * 20 + noise1.eval(
			x * 0.2,
			z * 0.2
		) * 10 + noise2.eval(x * 0.4, z * 0.4) * 5;
		double px = (double) x * SCALE_XZ + distortion1;
		double pz = (double) z * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		float height = getAverageDepth(biomeSource, x << 1, z << 1) * 0.5F;
		
		for (int y = 0; y < buffer.length; y++) {
			double py = (double) y * SCALE_Y;
			float dist = largeIslands.getDensity(px, py, pz, height);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz, height));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz, height));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			if (py > 100) {
				dist = (float) Mth.lerp((py - 100) / 27F, dist, -1);
			}
			buffer[y] = dist;
		}
		
		LOCKER.unlock();
	}
	
	public static float getTerrainDensity(int x, int y, int z, BiomeSource biomeSource) {
		LOCKER.lock();
		
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		double distortion1 = noise1.eval(x * 0.1, z * 0.1) * 20 + noise2.eval(
			x * 0.2,
			z * 0.2
		) * 10 + noise1.eval(x * 0.4, z * 0.4) * 5;
		double distortion2 = noise2.eval(x * 0.1, z * 0.1) * 20 + noise1.eval(
			x * 0.2,
			z * 0.2
		) * 10 + noise2.eval(x * 0.4, z * 0.4) * 5;
		double px = (double) x * SCALE_XZ + distortion1;
		double pz = (double) z * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		float height = getAverageDepth(biomeSource, x << 1, z << 1) * 0.5F;
		
		double py = (double) y * SCALE_Y;
		float dist = largeIslands.getDensity(px, py, pz, height);
		dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz, height));
		dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz, height));
		if (dist > -0.5F) {
			dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
			dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
			dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
		}
		if (py > 100) {
			dist = (float) Mth.lerp((py - 100) / 27F, dist, -1);
		}
		
		LOCKER.unlock();
		return dist;
	}
	
	private static float getAverageDepth(BiomeSource biomeSource, int x, int z) {
		/*if (getBiome(biomeSource, x, z).getTerrainHeight() < 0.1F) {
			return 0F;
		}
		float depth = 0F;
		for (int i = 0; i < OFFS.length; i++) {
			int px = x + OFFS[i].x;
			int pz = z + OFFS[i].y;
			depth += getBiome(biomeSource, px, pz).getTerrainHeight() * COEF[i];
		}
		return depth;*/
		return 0F;
	}
	
	private static BCLBiome getBiome(BiomeSource biomeSource, int x, int z) {
		// TODO replace null sampler
		return BiomeAPI.getBiome(biomeSource.getNoiseBiome(x, 0, z, null));
	}
	
	/**
	 * Check if this is land
	 *
	 * @param x - biome pos x
	 * @param z - biome pos z
	 */
	public static boolean isLand(int x, int z) {
		int sectionX = TerrainBoolCache.scaleCoordinate(x);
		int sectionZ = TerrainBoolCache.scaleCoordinate(z);
		
		LOCKER.lock();
		POS.setLocation(sectionX, sectionZ);
		
		TerrainBoolCache section = TERRAIN_BOOL_CACHE_MAP.get(POS);
		if (section == null) {
			if (TERRAIN_BOOL_CACHE_MAP.size() > 64) {
				TERRAIN_BOOL_CACHE_MAP.clear();
			}
			section = new TerrainBoolCache();
			TERRAIN_BOOL_CACHE_MAP.put(new Point(POS.x, POS.y), section);
		}
		byte value = section.getData(x, z);
		if (value > 0) {
			LOCKER.unlock();
			return value > 1;
		}
		
		double px = (x >> 1) + 0.5;
		double pz = (z >> 1) + 0.5;
		
		double distortion1 = noise1.eval(px * 0.1, pz * 0.1) * 20 + noise2.eval(px * 0.2, pz * 0.2) * 10 + noise1.eval(
			px * 0.4,
			pz * 0.4
		) * 5;
		double distortion2 = noise2.eval(px * 0.1, pz * 0.1) * 20 + noise1.eval(px * 0.2, pz * 0.2) * 10 + noise2.eval(
			px * 0.4,
			pz * 0.4
		) * 5;
		px = px * SCALE_XZ + distortion1;
		pz = pz * SCALE_XZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		boolean result = false;
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
				result = true;
				break;
			}
		}
		
		section.setData(x, z, (byte) (result ? 2 : 1));
		LOCKER.unlock();
		
		return result;
	}
	
	/**
	 * Get something like height
	 *
	 * @param x - block pos x
	 * @param z - block pos z
	 */
	public static int getHeight(int x, int z, BiomeSource biomeSource) {
		int posX = (x >> 3) << 3;
		int posZ = (z >> 3) << 3;
		float dx = (x - posX) / 8.0F;
		float dz = (z - posZ) / 8.0F;
		double[][][] buffer = new double[2][2][32];
		
		LOCKER.lock();
		for (int i = 0; i < 4; i++) {
			int ix = i & 1;
			int iz = i >> 1;
			int px = ((ix << 3) + posX) >> 3;
			int pz = ((iz << 3) + posZ) >> 3;
			fillTerrainDensity(buffer[ix][iz], px, pz, biomeSource);
		}
		LOCKER.unlock();
		
		for (int j = 30; j >= 0; j--) {
			float a = (float) buffer[0][0][j];
			float b = (float) buffer[1][0][j];
			float c = (float) buffer[0][1][j];
			float d = (float) buffer[1][1][j];
			
			float e = (float) buffer[0][0][j + 1];
			float f = (float) buffer[1][0][j + 1];
			float g = (float) buffer[0][1][j + 1];
			float h = (float) buffer[1][1][j + 1];
			
			if (a < 0 && b < 0 && c < 0 && d < 0 && e < 0 && f < 0 && g < 0 && h < 0) {
				continue;
			}
			
			a = Mth.lerp(dx, a, b);
			b = Mth.lerp(dx, c, d);
			c = Mth.lerp(dx, e, f);
			d = Mth.lerp(dx, g, h);
			
			a = Mth.lerp(dz, a, b);
			b = Mth.lerp(dz, c, d);
			
			for (int n = 7; n >= 0; n--) {
				float dy = n / 8.0F;
				float dens = Mth.lerp(dy, a, b);
				if (dens > 0) {
					return (j << 3 | n) + 1;
				}
			}
		}
		
		return -256;
	}
	
	static {
		float sum = 0;
		List<Float> coef = Lists.newArrayList();
		List<Point> pos = Lists.newArrayList();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				float dist = MHelper.length(x, z) / 3F;
				if (dist <= 1) {
					sum += dist;
					coef.add(dist);
					pos.add(new Point(x, z));
				}
			}
		}
		OFFS = pos.toArray(new Point[] {});
		COEF = new float[coef.size()];
		for (int i = 0; i < COEF.length; i++) {
			COEF[i] = coef.get(i) / sum;
		}
	}
}
