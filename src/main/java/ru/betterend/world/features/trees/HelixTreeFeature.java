package ru.betterend.world.features.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.world.features.DefaultFeature;

public class HelixTreeFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		float angle = random.nextFloat() * MHelper.PI2;
		float radiusRange = MHelper.randRange(4.5F, 6F, random);
		float scale = MHelper.randRange(0.5F, 1F, random);
		
		float dx;
		float dz;
		List<Vector3f> spline = new ArrayList<Vector3f>(10);
		for (int i = 0; i < 10; i++) {
			float radius = (0.9F - i * 0.1F) * radiusRange;
			dx = (float) Math.sin(i + angle) * radius;
			dz = (float) Math.cos(i + angle) * radius;
			spline.add(new Vector3f(dx, i * 2, dz));
		}
		SDF sdf = SplineHelper.buildSDF(spline, 1.7F, 0.5F, (p) -> { return EndBlocks.HELIX_TREE.bark.getDefaultState(); });
		SDF rotated = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, (float) Math.PI).setSource(sdf);
		sdf = new SDFUnion().setSourceA(rotated).setSourceB(sdf);
		
		Vector3f lastPoint = spline.get(spline.size() - 1);
		List<Vector3f> spline2 = SplineHelper.makeSpline(0, 0, 0, 0, 20, 0, 5);
		SDF stem = SplineHelper.buildSDF(spline2, 1.0F, 0.5F, (p) -> { return EndBlocks.HELIX_TREE.bark.getDefaultState(); });
		stem = new SDFTranslate().setTranslate(lastPoint.getX(), lastPoint.getY(), lastPoint.getZ()).setSource(stem);
		sdf = new SDFSmoothUnion().setRadius(3).setSourceA(sdf).setSourceB(stem);
		
		sdf = new SDFScale().setScale(scale).setSource(sdf);
		dx = 30 * scale;
		float dy1 = -20 * scale;
		float dy2 = 100 * scale;
		sdf.fillArea(world, pos, new Box(pos.add(-dx, dy1, -dx), pos.add(dx, dy2, dx)));
		SplineHelper.scale(spline, scale);
		SplineHelper.fillSplineForce(spline, world, EndBlocks.HELIX_TREE.bark.getDefaultState(), pos, (state) -> {
			return state.getMaterial().isReplaceable();
		});
		SplineHelper.rotateSpline(spline, (float) Math.PI);
		SplineHelper.fillSplineForce(spline, world, EndBlocks.HELIX_TREE.bark.getDefaultState(), pos, (state) -> {
			return state.getMaterial().isReplaceable();
		});
		SplineHelper.scale(spline2, scale);
		BlockPos leafStart = pos.add(lastPoint.getX() + 0.5, lastPoint.getY() + 0.5, lastPoint.getZ() + 0.5);
		SplineHelper.fillSplineForce(spline2, world, EndBlocks.HELIX_TREE.bark.getDefaultState(), leafStart, (state) -> {
			return state.getMaterial().isReplaceable();
		});
		
		spline.clear();
		for (int i = 0; i <= 20; i++) {
			float radius = i * 0.1F - 1;
			radius *= radius;
			radius = (1 - radius) * 4F * scale;
			dx = (float) Math.sin(i * 0.25F + angle) * radius;
			dz = (float) Math.cos(i * 0.25F + angle) * radius;
			spline.add(new Vector3f(dx, i * scale, dz));
		}
		
		Vector3f start = new Vector3f();
		Vector3f end = new Vector3f();
		lastPoint = spline.get(0);
		for (int i = 1; i < spline.size(); i++) {
			Vector3f point = spline.get(i);
			int minY = MHelper.floor(lastPoint.getY());
			int maxY = MHelper.floor(point.getY());
			float div = point.getY() - lastPoint.getY();
			for (float py = minY; py <= maxY; py += 0.25F) {
			//for (int py = minY; py <= maxY; py ++) {
				start.set(0, py, 0);
				float delta = (float) (py - minY) / div;
				float px = MathHelper.lerp(delta, lastPoint.getX(), point.getX());
				float pz = MathHelper.lerp(delta, lastPoint.getZ(), point.getZ());
				end.set(px, py, pz);
				SplineHelper.fillLineForce(start, end, world, EndBlocks.HELIX_TREE_LEAVES.getDefaultState(), leafStart, (state) -> {
					return state.getMaterial().isReplaceable();
				});
			}
			lastPoint = point;
		}
		
		return true;
	}
}
