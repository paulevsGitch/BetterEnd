package ru.betterend.world.generator;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFRadialNoiseMap;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IslandLayer {
	private static final Random RANDOM = new Random();
	private final SDFRadialNoiseMap noise;
	private final SDF island;
	
	private final List<BlockPos> positions = new ArrayList<BlockPos>(9);
	private final Map<BlockPos, SDF> islands = Maps.newHashMap();
	private final OpenSimplexNoise density;
	private final int seed;
	private int lastX = Integer.MIN_VALUE;
	private int lastZ = Integer.MIN_VALUE;
	private final LayerOptions options;
	
	public IslandLayer(int seed, LayerOptions options) {
		this.density = new OpenSimplexNoise(seed);
		this.options = options;
		this.seed = seed;
		
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		noise = (SDFRadialNoiseMap) new SDFRadialNoiseMap().setSeed(seed).setRadius(0.5F).setIntensity(0.2F).setSource(coneTop);
		island = new SDFSmoothUnion().setRadius(0.01F).setSourceA(noise).setSourceB(coneBottom);
	}
	
	private int getSeed(int x, int z) {
		int h = seed + x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public void updatePositions(double x, double z) {
		int ix = MHelper.floor(x / options.distance);
		int iz = MHelper.floor(z / options.distance);
		
		if (lastX != ix || lastZ != iz) {
			lastX = ix;
			lastZ = iz;
			positions.clear();
			for (int pox = -1; pox < 2; pox++) {
				int px = pox + ix;
				long px2 = px;
				for (int poz = -1; poz < 2; poz++) {
					int pz = poz + iz;
					long pz2 = pz;
					if (GeneratorOptions.noRingVoid() || px2 * px2 + pz2 * pz2 > options.centerDist) {
						RANDOM.setSeed(getSeed(px, pz));
						double posX = (px + RANDOM.nextFloat()) * options.distance;
						double posY = MHelper.randRange(options.minY, options.maxY, RANDOM);
						double posZ = (pz + RANDOM.nextFloat()) * options.distance;
						if (density.eval(posX * 0.01, posZ * 0.01) > options.coverage) {
							positions.add(new BlockPos(posX, posY, posZ));
						}
					}
				}
			}
		}
		
		if (GeneratorOptions.hasCentralIsland() && Math.abs(ix) < GeneratorOptions.getIslandDistChunk() && Math.abs(iz) < GeneratorOptions.getIslandDistChunk()) {
			int count = positions.size();
			for (int n = 0; n < count; n++) {
				BlockPos pos = positions.get(n);
				long d = (long) pos.getX() * (long) pos.getX() + (long) pos.getZ() * (long) pos.getZ();
				if (d < GeneratorOptions.getIslandDistBlock()) {
					positions.remove(n);
					count--;
					n--;
				}
			}
			if (options.hasCentralIsland) {
				positions.add(new BlockPos(0, 64, 0));
			}
		}
	}
	
	private SDF getIsland(BlockPos pos) {
		SDF island = islands.get(pos);
		if (island == null) {
			if (pos.getX() == 0 && pos.getZ() == 0) {
				island = new SDFScale().setScale(1.3F).setSource(this.island);
			}
			else {
				RANDOM.setSeed(getSeed(pos.getX(), pos.getZ()));
				island = new SDFScale().setScale(RANDOM.nextFloat() + 0.5F).setSource(this.island);
			}
			islands.put(pos, island);
		}
		noise.setOffset(pos.getX(), pos.getZ());
		return island;
	}
	
	private float getRelativeDistance(SDF sdf, BlockPos center, double px, double py, double pz) {
		float x = (float) (px - center.getX()) / options.scale;
		float y = (float) (py - center.getY()) / options.scale;
		float z = (float) (pz - center.getZ()) / options.scale;
		return sdf.getDistance(x, y, z);
	}
	
	private float calculateSDF(double x, double y, double z) {
		float distance = 10;
		for (BlockPos pos : positions) {
			SDF island = getIsland(pos);
			float dist = getRelativeDistance(island, pos, x, y, z);
			distance = MHelper.min(distance, dist);
		}
		return distance;
	}
	
	public float getDensity(double x, double y, double z) {
		return -calculateSDF(x, y, z);
	}
	
	public float getDensity(double x, double y, double z, float height) {
		noise.setIntensity(height);
		noise.setRadius(0.5F / (1 + height));
		return -calculateSDF(x, y, z);
	}
	
	public void clearCache() {
		if (islands.size() > 128) {
			islands.clear();
		}
	}
	
	private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
		float hh = height * 0.5F;
		SDF sdf = new SDFCappedCone().setHeight(hh).setRadius1(radiusBottom).setRadius2(radiusTop);
		return new SDFTranslate().setTranslate(0, minY + hh, 0).setSource(sdf);
	}
	
	/*private static NativeImage loadMap(String path) {
		InputStream stream = IslandLayer.class.getResourceAsStream(path);
		if (stream != null) {
			try {
				NativeImage map = NativeImage.read(stream);
				stream.close();
				return map;
			}
			catch (IOException e) {
				BetterEnd.LOGGER.warning(e.getMessage());
			}
		}
		return null;
	}*/
	
	/*static {
		NativeImage map = loadMap("/assets/" + BetterEnd.MOD_ID + "/textures/heightmaps/mountain_1.png");
		
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		
		SDF map1 = new SDFHeightmap().setMap(map).setIntensity(0.3F).setSource(coneTop);
		NOISE = (SDFRadialNoiseMap) new SDFRadialNoiseMap().setSource(coneTop);
		
		ISLAND = new SDF[] {
			new SDFSmoothUnion().setRadius(0.01F).setSourceA(coneTop).setSourceB(coneBottom),
			new SDFSmoothUnion().setRadius(0.01F).setSourceA(map1).setSourceB(coneBottom)
		};
	}*/
}
