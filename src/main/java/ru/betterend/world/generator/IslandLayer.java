package ru.betterend.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.util.math.BlockPos;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;

public class IslandLayer {
	private static final Random RANDOM = new Random();
	private static final SDF ISLAND;
	
	private final List<BlockPos> positions = new ArrayList<BlockPos>(9);
	private final Map<BlockPos, SDF> islands = Maps.newHashMap();
	private final OpenSimplexNoise density;
	private final double distance;
	private final float scale;
	private final int seed;
	private final int minY;
	private final int maxY;
	
	public IslandLayer(int seed, double distance, float scale, int center, int heightVariation) {
		this.distance = distance;
		this.density = new OpenSimplexNoise(seed);
		this.scale = scale;
		this.seed = seed;
		this.minY = center - heightVariation;
		this.maxY = center + heightVariation;
	}
	
	private int getSeed(int x, int z) {
		int h = seed + x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public void updatePositions(double x, double z) {
		positions.clear();
		
		int ix = MHelper.floor(x / distance);
		int iz = MHelper.floor(z / distance);
		
		for (int pox = -1; pox < 2; pox++) {
			int px = pox + ix;
			for (int poz = -1; poz < 2; poz++) {
				int pz = poz + iz;
				//if (density.eval(px * distance * 0.002, pz * distance * 0.002) > 0) {
					RANDOM.setSeed(getSeed(px, pz));
					double posX = (px + RANDOM.nextFloat()) * distance;
					double posY = MHelper.randRange(minY, maxY, RANDOM);
					double posZ = (pz + RANDOM.nextFloat()) * distance;
					positions.add(new BlockPos(posX, posY, posZ));
				//}
			}
		}
	}
	
	private SDF getIsland(BlockPos pos) {
		SDF island = islands.get(pos);
		if (island == null) {
			RANDOM.setSeed(getSeed(pos.getX(), pos.getZ()));
			island = new SDFScale().setScale(RANDOM.nextFloat() + 0.5F).setSource(ISLAND);
			islands.put(pos, island);
		}
		return island;
	}
	
	private float getRelativeDistance(SDF sdf, BlockPos center, double px, double py, double pz) {
		float x = (float) (px - center.getX()) / scale;
		float y = (float) (py - center.getY()) / scale;
		float z = (float) (pz - center.getZ()) / scale;
		return sdf.getDistance(x, y, z);
	}
	
	private float calculateSDF(double x, double y, double z) {
		float distance = 10;
		for (BlockPos pos: positions) {
			SDF island = getIsland(pos);
			float dist = getRelativeDistance(island, pos, x, y, z);
			distance = MHelper.min(distance, dist);
		}
		return distance;
	}
	
	public float getDensity(double x, double y, double z) {
		return -calculateSDF(x, y, z);
	}
	
	public void clearCache() {
		if (islands.size() > 32) {
			islands.clear();
		}
	}
	
	private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
		float hh = height * 0.5F;
		SDF sdf = new SDFCapedCone().setHeight(hh).setRadius1(radiusBottom).setRadius2(radiusTop);
		return new SDFTranslate().setTranslate(0, minY + hh, 0).setSource(sdf);
	}
	
	static {
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFUnion().setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFUnion().setSourceA(cone3).setSourceB(cone4);
		
		ISLAND = new SDFSmoothUnion().setRadius(0.01F).setSourceA(coneTop).setSourceB(coneBottom);
	}
}
