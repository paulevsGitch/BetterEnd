package ru.betterend.world.features.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.PosInfo;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.operator.SDFUnion;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.blocks.HelixTreeLeavesBlock;
import ru.betterend.registry.EndBlocks;

public class HelixTreeFeature extends DefaultFeature {
	private static final Function<PosInfo, BlockState> POST;

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (!world.getBlockState(pos.below()).is(TagAPI.END_GROUND))
			return false;
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
		SDF sdf = SplineHelper.buildSDF(spline, 1.7F, 0.5F, (p) -> {
			return EndBlocks.HELIX_TREE.bark.defaultBlockState();
		});
		SDF rotated = new SDFRotation().setRotation(Vector3f.YP, (float) Math.PI).setSource(sdf);
		sdf = new SDFUnion().setSourceA(rotated).setSourceB(sdf);

		Vector3f lastPoint = spline.get(spline.size() - 1);
		List<Vector3f> spline2 = SplineHelper.makeSpline(0, 0, 0, 0, 20, 0, 5);
		SDF stem = SplineHelper.buildSDF(spline2, 1.0F, 0.5F, (p) -> {
			return EndBlocks.HELIX_TREE.bark.defaultBlockState();
		});
		stem = new SDFTranslate().setTranslate(lastPoint.x(), lastPoint.y(), lastPoint.z()).setSource(stem);
		sdf = new SDFSmoothUnion().setRadius(3).setSourceA(sdf).setSourceB(stem);

		sdf = new SDFScale().setScale(scale).setSource(sdf);
		dx = 30 * scale;
		float dy1 = -20 * scale;
		float dy2 = 100 * scale;
		sdf.addPostProcess(POST).fillArea(world, pos, new AABB(pos.offset(-dx, dy1, -dx), pos.offset(dx, dy2, dx)));
		SplineHelper.scale(spline, scale);
		SplineHelper.fillSplineForce(spline, world, EndBlocks.HELIX_TREE.bark.defaultBlockState(), pos, (state) -> {
			return state.getMaterial().isReplaceable();
		});
		SplineHelper.rotateSpline(spline, (float) Math.PI);
		SplineHelper.fillSplineForce(spline, world, EndBlocks.HELIX_TREE.bark.defaultBlockState(), pos, (state) -> {
			return state.getMaterial().isReplaceable();
		});
		SplineHelper.scale(spline2, scale);
		BlockPos leafStart = pos.offset(lastPoint.x() + 0.5, lastPoint.y() + 0.5, lastPoint.z() + 0.5);
		SplineHelper.fillSplineForce(spline2, world, EndBlocks.HELIX_TREE.log.defaultBlockState(), leafStart,
				(state) -> {
					return state.getMaterial().isReplaceable();
				});

		spline.clear();
		float rad = MHelper.randRange(8F, 11F, random);
		int count = MHelper.randRange(20, 30, random);
		float scaleM = 20F / (float) count * scale * 1.75F;
		float hscale = 20F / (float) count * 0.05F;
		for (int i = 0; i <= count; i++) {
			float radius = 1 - i * hscale;
			radius = radius * radius * 2 - 1;
			radius *= radius;
			radius = (1 - radius) * rad * scale;
			dx = (float) Math.sin(i * 0.45F + angle) * radius;
			dz = (float) Math.cos(i * 0.45F + angle) * radius;
			spline.add(new Vector3f(dx, i * scaleM, dz));
		}

		Vector3f start = new Vector3f();
		Vector3f end = new Vector3f();
		lastPoint = spline.get(0);
		BlockState leaf = EndBlocks.HELIX_TREE_LEAVES.defaultBlockState();
		for (int i = 1; i < spline.size(); i++) {
			Vector3f point = spline.get(i);
			int minY = MHelper.floor(lastPoint.y());
			int maxY = MHelper.floor(point.y());
			float div = point.y() - lastPoint.y();
			for (float py = minY; py <= maxY; py += 0.2F) {
				start.set(0, py, 0);
				float delta = (float) (py - minY) / div;
				float px = Mth.lerp(delta, lastPoint.x(), point.x());
				float pz = Mth.lerp(delta, lastPoint.z(), point.z());
				end.set(px, py, pz);
				fillLine(start, end, world, leaf, leafStart, i / 2 - 1);
				float ax = Math.abs(px);
				float az = Math.abs(pz);
				if (ax > az) {
					start.set(start.x(), start.y(), start.z() + az > 0 ? 1 : -1);
					end.set(end.x(), end.y(), end.z() + az > 0 ? 1 : -1);
				} else {
					start.set(start.x() + ax > 0 ? 1 : -1, start.y(), start.z());
					end.set(end.x() + ax > 0 ? 1 : -1, end.y(), end.z());
				}
				fillLine(start, end, world, leaf, leafStart, i / 2 - 1);
			}
			lastPoint = point;
		}

		leaf = leaf.setValue(HelixTreeLeavesBlock.COLOR, 7);
		leafStart = leafStart.offset(0, lastPoint.y(), 0);
		if (world.getBlockState(leafStart).isAir()) {
			BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
			leafStart = leafStart.above();
			if (world.getBlockState(leafStart).isAir()) {
				BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
				leafStart = leafStart.above();
				if (world.getBlockState(leafStart).isAir()) {
					BlocksHelper.setWithoutUpdate(world, leafStart, leaf);
				}
			}
		}

		return true;
	}

	private void fillLine(Vector3f start, Vector3f end, WorldGenLevel world, BlockState state, BlockPos pos,
			int offset) {
		float dx = end.x() - start.x();
		float dy = end.y() - start.y();
		float dz = end.z() - start.z();
		float max = MHelper.max(Math.abs(dx), Math.abs(dy), Math.abs(dz));
		int count = MHelper.floor(max + 1);
		dx /= max;
		dy /= max;
		dz /= max;
		float x = start.x();
		float y = start.y();
		float z = start.z();

		MutableBlockPos bPos = new MutableBlockPos();
		for (int i = 0; i < count; i++) {
			bPos.set(x + pos.getX(), y + pos.getY(), z + pos.getZ());
			int color = MHelper.floor((float) i / (float) count * 7F + 0.5F) + offset;
			color = Mth.clamp(color, 0, 7);
			if (world.getBlockState(bPos).getMaterial().isReplaceable()) {
				BlocksHelper.setWithoutUpdate(world, bPos, state.setValue(HelixTreeLeavesBlock.COLOR, color));
			}
			x += dx;
			y += dy;
			z += dz;
		}
		bPos.set(end.x() + pos.getX(), end.y() + pos.getY(), end.z() + pos.getZ());
		if (world.getBlockState(bPos).getMaterial().isReplaceable()) {
			BlocksHelper.setWithoutUpdate(world, bPos, state.setValue(HelixTreeLeavesBlock.COLOR, 7));
		}
	}

	static {
		POST = (info) -> {
			if (EndBlocks.HELIX_TREE.isTreeLog(info.getStateUp())
					&& EndBlocks.HELIX_TREE.isTreeLog(info.getStateDown())) {
				return EndBlocks.HELIX_TREE.log.defaultBlockState();
			}
			return info.getState();
		};
	}
}
