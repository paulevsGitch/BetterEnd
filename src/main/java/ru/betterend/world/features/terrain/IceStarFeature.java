package ru.betterend.world.features.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;
import ru.betterend.world.features.DefaultFeature;

public class IceStarFeature extends DefaultFeature {
	private final float minSize;
	private final float maxSize;
	private final int minCount;
	private final int maxCount;
	
	public IceStarFeature(float minSize, float maxSize, int minCount, int maxCount) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minCount = minCount;
		this.maxCount = maxCount;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		float size = MHelper.randRange(minSize, maxSize, random);
		int count = MHelper.randRange(minCount, maxCount, random);
		List<Vector3f> points = getFibonacciPoints(count);
		SDF sdf = null;
		SDF spike = new SDFCapedCone().setRadius1(3 + (size - 5) * 0.2F).setRadius2(0).setHeight(size).setBlock(Blocks.ICE);
		spike = new SDFTranslate().setTranslate(0, size - 0.5F, 0).setSource(spike);
		for (Vector3f point: points) {
			SDF rotated = spike;
			point = MHelper.normalize(point);
			float angle = MHelper.angle(Vector3f.POSITIVE_Y, point);
			if (angle > 0.01F && angle < 3.14F) {
				Vector3f axis = MHelper.normalize(MHelper.cross(Vector3f.POSITIVE_Y, point));
				rotated = new SDFRotation().setRotation(axis, angle).setSource(spike);
			}
			else if (angle > 1) {
				rotated = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, (float) Math.PI).setSource(spike);
			}
			sdf = (sdf == null) ? rotated : new SDFUnion().setSourceA(sdf).setSourceB(rotated);
		}
		
		int x1 = (pos.getX() >> 4) << 4;
		int z1 = (pos.getZ() >> 4) << 4;
		pos = new BlockPos(x1 + random.nextInt(16), MHelper.randRange(32, 128, random), z1 + random.nextInt(16));
		
		OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextLong());
		
		final boolean hasSnow = random.nextBoolean();
		BlockState layer = Blocks.SNOW.getDefaultState();
		BlockState snow = Blocks.SNOW_BLOCK.getDefaultState();
		BlockState blue = Blocks.BLUE_ICE.getDefaultState();
		BlockState packed = Blocks.PACKED_ICE.getDefaultState();
		sdf.setPostProcess((info) -> {
			BlockPos bpos = info.getPos();

			if (!info.getState().isOf(Blocks.SNOW)) {
				if (hasSnow && info.getStateUp().isAir()) {
					info.setBlockPos(bpos.up(), layer.with(Properties.LAYERS, MHelper.randRange(1, 3, random)));
					return snow;
				}

				if (noise1.eval(bpos.getX() * 0.1, bpos.getY() * 0.1, bpos.getZ() * 0.1) > 0.3) {
					return packed;
				}
				else if (noise2.eval(bpos.getX() * 0.1, bpos.getY() * 0.1, bpos.getZ() * 0.1) > 0.3) {
					return blue;
				}
			}
			return info.getState();
		}).fillRecursive(world, pos);

		return true;
	}

	private List<Vector3f> getFibonacciPoints(int count) {
		float max = count - 1;
		List<Vector3f> result = new ArrayList<Vector3f>(count);
		for (int i = 0; i < count; i++) {
			float y = 1F - (i / max) * 2F;
			float radius = (float) Math.sqrt(1F - y * y);
			float theta = MHelper.PHI * i;
			float x = (float) Math.cos(theta) * radius;
			float z = (float) Math.sin(theta) * radius;
			result.add(new Vector3f(x, y, z));
		}
		return result;
	}
}
