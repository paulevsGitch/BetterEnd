package ru.betterend.world.generator;

import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.NoiseChunk.NoiseFiller;

public class EndNoiseFiller implements NoiseFiller {
	public static final EndNoiseFiller INSTANCE = new EndNoiseFiller();
	private double[][][] noiseColumns = new double[3][3][33];
	private BiomeSource biomeSource;
	private int chunkX;
	private int chunkZ;
	
	private EndNoiseFiller() {}
	
	public void setBiomeSource(BiomeSource biomeSource) {
		this.biomeSource = biomeSource;
	}
	
	@Override
	public double calculateNoise(int x, int y, int z) {
		if (y < 0 || y > 127) {
			return -10;
		}
		
		int cx = x >> 4;
		int cz = z >> 4;
		if (chunkX != cx || chunkZ != cz) {
			chunkX = cx;
			chunkZ = cz;
			int px = cx << 1;
			int pz = cz << 1;
			for (byte i = 0; i < 3; i++) {
				for (byte j = 0; j < 3; j++) {
					TerrainGenerator.fillTerrainDensity(noiseColumns[i][j], px + i, pz + j, biomeSource);
				}
			}
		}
		
		byte ix = (byte) ((x & 15) >> 3);
		byte iy = (byte) (y >> 2);
		byte iz = (byte) ((z & 15) >> 3);
		float dx = (x & 7) / 8F;
		float dy = (y & 3) / 4F;
		float dz = (z & 7) / 8F;
		
		float a = (float) noiseColumns[ix][iz][iy];
		float b = (float) noiseColumns[ix + 1][iz][iy];
		float c = (float) noiseColumns[ix][iz][iy + 1];
		float d = (float) noiseColumns[ix + 1][iz][iy + 1];
		
		float e = (float) noiseColumns[ix][iz + 1][iy];
		float f = (float) noiseColumns[ix + 1][iz + 1][iy];
		float g = (float) noiseColumns[ix][iz + 1][iy + 1];
		float h = (float) noiseColumns[ix + 1][iz + 1][iy + 1];
		
		a = Mth.lerp(dx, a, b);
		b = Mth.lerp(dx, c, d);
		c = Mth.lerp(dx, e, f);
		d = Mth.lerp(dx, g, h);
		
		a = Mth.lerp(dy, a, b);
		b = Mth.lerp(dy, c, d);
		
		return Mth.lerp(dz, a, b);
	}
}
