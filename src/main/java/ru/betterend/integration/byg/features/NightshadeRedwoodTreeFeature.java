package ru.betterend.integration.byg.features;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.PosInfo;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFFlatWave;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.integration.Integrations;

public class NightshadeRedwoodTreeFeature extends DefaultFeature {
	private static final List<Vector3f> BRANCH;

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (!world.getBlockState(pos.below()).is(TagAPI.END_GROUND))
			return false;

		BlockState log = Integrations.BYG.getDefaultState("nightshade_log");
		BlockState wood = Integrations.BYG.getDefaultState("nightshade_wood");
		BlockState leaves = Integrations.BYG.getDefaultState("nightshade_leaves");
		BlockState leaves_flower = Integrations.BYG.getDefaultState("flowering_nightshade_leaves");

		Function<BlockPos, BlockState> splinePlacer = (bpos) -> {
			return log;
		};
		Function<BlockState, Boolean> replace = (state) -> {
			return state.is(TagAPI.END_GROUND) || state.getMaterial().equals(Material.PLANT)
					|| state.getMaterial().isReplaceable();
		};
		Function<PosInfo, BlockState> post = (info) -> {
			if (info.getState().equals(log) && (!info.getStateUp().equals(log) || !info.getStateDown().equals(log))) {
				return wood;
			}
			return info.getState();
		};
		Function<BlockState, Boolean> ignore = (state) -> {
			return state.equals(log) || state.equals(wood);
		};

		int height = MHelper.randRange(40, 60, random);
		List<Vector3f> trunk = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, height / 4);
		SplineHelper.offsetParts(trunk, random, 0.8F, 0, 0.8F);

		if (!SplineHelper.canGenerate(trunk, pos, world, replace)) {
			return false;
		}

		int count = height >> 2;
		float start = trunk.size() / 3F;
		float delta = trunk.size() * 0.6F;
		float max = height - 7;
		float startAngle = random.nextFloat() * MHelper.PI2;
		for (int i = 0; i < count; i++) {
			float scale = (float) (count - i) / count * 15;
			Vector3f offset = SplineHelper.getPos(trunk, (float) i / count * delta + start);
			if (offset.y() > max) {
				break;
			}
			List<Vector3f> branch = SplineHelper.copySpline(BRANCH);
			SplineHelper.rotateSpline(branch, i * 1.3F + startAngle);
			SplineHelper.scale(branch, scale);
			SplineHelper.offsetParts(branch, random, 0.3F, 0.3F, 0.3F);
			SplineHelper.offset(branch, offset);
			SplineHelper.fillSpline(branch, world, wood, pos, replace);
		}

		SDF sdf = SplineHelper.buildSDF(trunk, 2.3F, 0.8F, splinePlacer);
		SDF roots = new SDFSphere().setRadius(2F).setBlock(log);
		roots = new SDFFlatWave().setIntensity(2F).setRaysCount(MHelper.randRange(5, 7, random))
				.setAngle(random.nextFloat() * MHelper.PI2).setSource(roots);
		sdf = new SDFSmoothUnion().setRadius(2F).setSourceA(sdf).setSourceB(roots);
		sdf.setReplaceFunction(replace).addPostProcess(post).fillRecursive(world, pos);
		Vector3f last = SplineHelper.getPos(trunk, trunk.size() - 1.35F);
		for (int y = 0; y < 8; y++) {
			BlockPos p = pos.offset(last.x() + 0.5, last.y() + y, last.z() + 0.5);
			BlocksHelper.setWithoutUpdate(world, p, y == 4 ? wood : log);
		}

		for (int y = 0; y < 16; y++) {
			BlockPos p = pos.offset(last.x() + 0.5, last.y() + y, last.z() + 0.5);
			if (world.isEmptyBlock(p)) {
				BlocksHelper.setWithoutUpdate(world, p, leaves);
			}
			float radius = (1 - y / 16F) * 3F;
			int rad = (int) (radius + 1);
			radius *= radius;
			for (int x = -rad; x <= rad; x++) {
				int x2 = x * x;
				for (int z = -rad; z <= rad; z++) {
					int z2 = z * z;
					if (x2 + z2 < radius - random.nextFloat() * rad) {
						BlockPos lp = p.offset(x, 0, z);
						if (world.isEmptyBlock(lp)) {
							BlocksHelper.setWithoutUpdate(world, lp, leaves);
						}
					}
				}
			}
		}

		MutableBlockPos mut = new MutableBlockPos();
		Function<PosInfo, BlockState> leavesPost1 = (info) -> {
			if (info.getState().equals(log) || info.getState().equals(wood)) {
				for (int x = -6; x < 7; x++) {
					int ax = Math.abs(x);
					mut.setX(x + info.getPos().getX());
					for (int z = -6; z < 7; z++) {
						int az = Math.abs(z);
						mut.setZ(z + info.getPos().getZ());
						for (int y = -6; y < 7; y++) {
							int ay = Math.abs(y);
							int d = ax + ay + az;
							if (d < 7) {
								mut.setY(y + info.getPos().getY());
								BlockState state = info.getState(mut);
								if (state.getBlock() instanceof LeavesBlock) {
									int distance = state.getValue(LeavesBlock.DISTANCE);
									if (d < distance) {
										info.setState(mut, state.setValue(LeavesBlock.DISTANCE, d));
									}
								}
							}
						}
					}
				}
			}
			return info.getState();
		};
		Function<PosInfo, BlockState> leavesPost2 = (info) -> {
			if (info.getState().getBlock() instanceof LeavesBlock) {
				int distance = info.getState().getValue(LeavesBlock.DISTANCE);
				if (distance > MHelper.randRange(2, 4, random)) {
					return Blocks.AIR.defaultBlockState();
				}
				for (Direction d : BlocksHelper.DIRECTIONS) {
					int airCount = 0;
					if (info.getState(d).isAir()) {
						airCount++;
					}
					if (airCount > 5) {
						return Blocks.AIR.defaultBlockState();
					}
				}
				if (random.nextInt(8) == 0) {
					return leaves_flower.setValue(LeavesBlock.DISTANCE, distance);
				}
			}
			return info.getState();
		};

		SDF canopy = new SDFCappedCone().setRadius1(12F).setRadius2(1f).setHeight(height * 0.3F).setBlock(leaves);
		canopy = new SDFDisplacement().setFunction((vec) -> {
			return MHelper.randRange(-3F, 3F, random);
		}).setSource(canopy);
		canopy.addPostProcess(leavesPost1).addPostProcess(leavesPost2).fillRecursiveIgnore(world,
				pos.offset(0, height * 0.75, 0), ignore);

		return true;
	}

	static {
		BRANCH = Lists.newArrayList(new Vector3f(0, 0, 0), new Vector3f(0.25F, 0.1F, 0), new Vector3f(0.40F, 0.2F, 0),
				new Vector3f(0.50F, 0.4F, 0), new Vector3f(0.55F, 0.6F, 0));
	}
}
