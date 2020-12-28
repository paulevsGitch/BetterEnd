package ru.betterend.noise;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import ru.betterend.util.MHelper;

public class VoronoiNoise {
	private static final Random RANDOM = new Random();
	final int seed;
	
	public VoronoiNoise() {
		this(0);
	}

	public VoronoiNoise(int seed) {
		this.seed = seed;
	}
	
	private int getSeed(int x, int y, int z) {
		int h = seed + x * 374761393 + y * 668265263 + z;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}

	public double sample(double x, double y, double z) {
		int ix = MHelper.floor(x);
		int iy = MHelper.floor(y);
		int iz = MHelper.floor(z);
		
		float px = (float) (x - ix);
		float py = (float) (y - iy);
		float pz = (float) (z - iz);
		
		float d = 10;
		
		for (int pox = -1; pox < 2; pox++) {
			for (int poy = -1; poy < 2; poy++) {
				for (int poz = -1; poz < 2; poz++) {
					RANDOM.setSeed(getSeed(pox + ix, poy + iy, poz + iz));
					float pointX = pox + RANDOM.nextFloat();
					float pointY = poy + RANDOM.nextFloat();
					float pointZ = poz + RANDOM.nextFloat();
					float d2 = MHelper.lengthSqr(pointX - px, pointY - py, pointZ - pz);
					if (d2 < d) {
						d = d2;
					}
				}
			}
		}
		
		return Math.sqrt(d);
	}
	
	public BlockPos[] getPos(double x, double y, double z, double scale) {
		int ix = MHelper.floor(x);
		int iy = MHelper.floor(y);
		int iz = MHelper.floor(z);
		
		float px = (float) (x - ix);
		float py = (float) (y - iy);
		float pz = (float) (z - iz);
		
		float d = 10;
		float selX = 0;
		float selY = 0;
		float selZ = 0;
		float selXPre = 0;
		float selYPre = 0;
		float selZPre = 0;
		
		for (int pox = -1; pox < 2; pox++) {
			for (int poy = -1; poy < 2; poy++) {
				for (int poz = -1; poz < 2; poz++) {
					RANDOM.setSeed(getSeed(pox + ix, poy + iy, poz + iz));
					float pointX = pox + RANDOM.nextFloat();
					float pointY = poy + RANDOM.nextFloat();
					float pointZ = poz + RANDOM.nextFloat();
					float d2 = MHelper.lengthSqr(pointX - px, pointY - py, pointZ - pz);
					if (d2 < d) {
						d = d2;
						selXPre = selX;
						selYPre = selY;
						selZPre = selZ;
						selX = pointX;
						selY = pointY;
						selZ = pointZ;
					}
				}
			}
		}
		
		BlockPos p1 = new BlockPos((ix + (double) selX) * scale, (iy + (double) selY) * scale, (iz + (double) selZ) * scale);
		BlockPos p2 = new BlockPos((ix + (double) selXPre) * scale, (iy + (double) selYPre) * scale, (iz + (double) selZPre) * scale);
		return new BlockPos[] {p1, p2};
	}
}
