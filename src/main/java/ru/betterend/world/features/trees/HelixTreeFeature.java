package ru.betterend.world.features.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockHelixTreeLeaves;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
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
		BlocksHelper.setWithoutUpdate(world, pos, AIR);
		
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
			float radius = 1 - i * 0.05F;
			radius = radius * radius * 2 - 1;
			radius *= radius;
			radius = (1 - radius) * 8F * scale;
			dx = (float) Math.sin(i * 0.45F + angle) * radius;
			dz = (float) Math.cos(i * 0.45F + angle) * radius;
			spline.add(new Vector3f(dx, i * scale * 1.75F, dz));
		}
		
		Vector3f start = new Vector3f();
		Vector3f end = new Vector3f();
		lastPoint = spline.get(0);
		BlockState leaf = EndBlocks.HELIX_TREE_LEAVES.getDefaultState();
		for (int i = 1; i < spline.size(); i++) {
			Vector3f point = spline.get(i);
			int minY = MHelper.floor(lastPoint.getY());
			int maxY = MHelper.floor(point.getY());
			float div = point.getY() - lastPoint.getY();
			for (float py = minY; py <= maxY; py += 0.2F) {
				start.set(0, py, 0);
				float delta = (float) (py - minY) / div;
				float px = MathHelper.lerp(delta, lastPoint.getX(), point.getX());
				float pz = MathHelper.lerp(delta, lastPoint.getZ(), point.getZ());
				end.set(px, py, pz);
				fillLine(start, end, world, leaf, leafStart, i / 2 - 1);
				float ax = Math.abs(px);
				float az = Math.abs(pz);
				if (ax > az) {
					start.set(start.getX(), start.getY(), start.getZ() + az > 0 ? 1 : -1);
					end.set(end.getX(), end.getY(), end.getZ() + az > 0 ? 1 : -1);
				}
				else {
					start.set(start.getX() + ax > 0 ? 1 : -1, start.getY(), start.getZ());
					end.set(end.getX() + ax > 0 ? 1 : -1, end.getY(), end.getZ());
				}
				fillLine(start, end, world, leaf, leafStart, i / 2 - 1);
			}
			lastPoint = point;
		}
		
		leaf = leaf.with(BlockHelixTreeLeaves.COLOR, 7);
		leafStart = leafStart.add(0, lastPoint.getY(), 0);
		if (world.getBlockState(leafStart).isAir()) {
			BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
			leafStart = leafStart.up();
			if (world.getBlockState(leafStart).isAir()) {
				BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
				leafStart = leafStart.up();
						if (world.getBlockState(leafStart).isAir()) {
							BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
						}
			}
		}
		
		return true;
	}
	
	private void fillLine(Vector3f start, Vector3f end, StructureWorldAccess world, BlockState state, BlockPos pos, int offset) {
		float dx = end.getX() - start.getX();
		float dy = end.getY() - start.getY();
		float dz = end.getZ() - start.getZ();
		float max = MHelper.max(Math.abs(dx), Math.abs(dy), Math.abs(dz));
		int count = MHelper.floor(max + 1);
		dx /= max;
		dy /= max;
		dz /= max;
		float x = start.getX();
		float y = start.getY();
		float z = start.getZ();
		
		Mutable bPos = new Mutable();
		for (int i = 0; i < count; i++) {
			bPos.set(x + pos.getX(), y + pos.getY(), z + pos.getZ());
			int color = MHelper.floor((float) i / (float) count * 7F + 0.5F) + offset;
			color = MathHelper.clamp(color, 0, 7);
			if (world.getBlockState(bPos).getMaterial().isReplaceable()) {
				BlocksHelper.setWithoutUpdate(world, bPos, state.with(BlockHelixTreeLeaves.COLOR, color));
			}
			x += dx;
			y += dy;
			z += dz;
		}
		bPos.set(end.getX() + pos.getX(), end.getY() + pos.getY(), end.getZ() + pos.getZ());
		if (world.getBlockState(bPos).getMaterial().isReplaceable()) {
			BlocksHelper.setWithoutUpdate(world, bPos, state.with(BlockHelixTreeLeaves.COLOR, 7));
		}
	}
}
