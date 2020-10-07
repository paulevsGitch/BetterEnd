package ru.betterend.noise;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;

public class VoronoiNoise {
	private static final Random RANDOM = new Random();
	final int seed;
	final double scale;
	final double separation;

	public VoronoiNoise(int seed, double side, double separation) {
		this.seed = seed;
		this.scale = 1.0 / side;
		this.separation = MathHelper.clamp(separation, 0, 1);
	}
	
	private int getSeed(int x, int y, int z) {
		int h = seed + x * 374761393 + y * 668265263 + z;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public double sample(float x, float y, float z) {
		return sample(MHelper.floor(x), MHelper.floor(y), MHelper.floor(z));
	}

	public double sample(int x, int y, int z) {
		double posX = x * scale;
		double posY = y * scale;
		double posZ = z * scale;
		int posXI = MHelper.floor(posX);
		int posYI = MHelper.floor(posY);
		int posZI = MHelper.floor(posZ);
		double distance = Double.MAX_VALUE;
		for (int px = -1; px < 2; px++) {
			double pointX = posXI + px;
			for (int py = -1; py < 2; py++) {
				double pointY = posYI + py;
				for (int pz = -1; pz < 2; pz++) {
					double pointZ = posZI + pz;
					RANDOM.setSeed(getSeed(posXI + px, posYI + py, posZI + pz));
					
					double posXN = pointX + RANDOM.nextDouble() * separation;
					double posYN = pointY + RANDOM.nextDouble() * separation;
					double posZN = pointZ + RANDOM.nextDouble() * separation;
					
					double dist2 = MHelper.lengthSqr(posXN - posX, posYN - posY, posZN - posZ);
					if (dist2 < distance) {
						distance = dist2;
					}
				}
			}
		}
		return distance;
	}
}
