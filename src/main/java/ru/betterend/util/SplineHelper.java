package ru.betterend.util;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFLine;

public class SplineHelper {
	public static List<Vector3f> makeSpline(float x1, float y1, float z1, float x2, float y2, float z2, int points) {
		List<Vector3f> spline = Lists.newArrayList();
		spline.add(new Vector3f(x1, y1, z1));
		int count = points - 1;
		for (int i = 1; i < count; i++) {
			float delta = (float) i / (float) count;
			float x = MathHelper.lerp(delta, x1, x2);
			float y = MathHelper.lerp(delta, y1, y2);
			float z = MathHelper.lerp(delta, z1, z2);
			spline.add(new Vector3f(x, y, z));
		}
		spline.add(new Vector3f(x2, y2, z2));
		return spline;
	}
	
	public static void offsetParts(List<Vector3f> spline, Random random, float dx, float dy, float dz) {
		int count = spline.size();
		for (int i = 1; i < count; i++) {
			Vector3f pos = spline.get(i);
			float x = pos.getX() + (float) random.nextGaussian() * dx;
			float y = pos.getY() + (float) random.nextGaussian() * dy;
			float z = pos.getZ() + (float) random.nextGaussian() * dz;
			pos.set(x, y, z);
		}
	}
	
	public static SDF buildSDF(List<Vector3f> spline, float radius1, float radius2, Function<BlockPos, BlockState> placerFunction) {
		int count = spline.size();
		float max = count - 2;
		SDF result = null;
		Vector3f start = spline.get(0);
		for (int i = 1; i < count; i++) {
			Vector3f pos = spline.get(i);
			float delta = (float) (i - 1) / max;
			SDF line = new SDFLine()
					.setRadius(MathHelper.lerp(delta, radius1, radius2))
					.setStart(start.getX(), start.getY(), start.getZ())
					.setEnd(pos.getX(), pos.getY(), pos.getZ())
					.setBlock(placerFunction);
			result = result == null ? line : new SDFUnion().setSourceA(result).setSourceB(line);
			start = pos;
		}
		return result;
	}
}
