package ru.betterend.world.features.trees;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.sdf.PosInfo;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PythadendronTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final Function<PosInfo, BlockState> POST;
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (world.getBlockState(pos.below()).getBlock() != EndBlocks.CHORUS_NYLIUM) {
			return false;
		}
		BlocksHelper.setWithoutUpdate(world, pos, AIR);
		
		float size = MHelper.randRange(10, 20, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 4);
		SplineHelper.offsetParts(spline, random, 0.7F, 0, 0.7F);
		Vector3f last = spline.get(spline.size() - 1);
		
		int depth = MHelper.floor((size - 10F) * 3F / 10F + 1F);
		float bsize = (10F - (size - 10F)) / 10F + 1.5F;
		branch(
			last.x(),
			last.y(),
			last.z(),
			size * bsize,
			MHelper.randRange(0, MHelper.PI2, random),
			random,
			depth,
			world,
			pos
		);
		
		SDF function = SplineHelper.buildSDF(spline, 1.7F, 1.1F, (bpos) -> {
			return EndBlocks.PYTHADENDRON.getBark().defaultBlockState();
		});
		function.setReplaceFunction(REPLACE);
		function.addPostProcess(POST);
		function.fillRecursive(world, pos);
		
		return true;
	}
	
	private void branch(float x, float y, float z, float size, float angle, Random random, int depth, WorldGenLevel world, BlockPos pos) {
		if (depth == 0) return;
		
		float dx = (float) Math.cos(angle) * size * 0.15F;
		float dz = (float) Math.sin(angle) * size * 0.15F;
		
		float x1 = x + dx;
		float z1 = z + dz;
		float x2 = x - dx;
		float z2 = z - dz;
		
		List<Vector3f> spline = SplineHelper.makeSpline(x, y, z, x1, y, z1, 5);
		SplineHelper.powerOffset(spline, size * MHelper.randRange(1.0F, 2.0F, random), 4);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		Vector3f pos1 = spline.get(spline.size() - 1);
		
		boolean s1 = SplineHelper.fillSpline(
			spline,
			world,
			EndBlocks.PYTHADENDRON.getBark().defaultBlockState(),
			pos,
			REPLACE
		);
		
		spline = SplineHelper.makeSpline(x, y, z, x2, y, z2, 5);
		SplineHelper.powerOffset(spline, size * MHelper.randRange(1.0F, 2.0F, random), 4);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		Vector3f pos2 = spline.get(spline.size() - 1);
		
		boolean s2 = SplineHelper.fillSpline(
			spline,
			world,
			EndBlocks.PYTHADENDRON.getBark().defaultBlockState(),
			pos,
			REPLACE
		);
		
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		if (depth < 3) {
			if (s1) {
				leavesBall(world, pos.offset(pos1.x(), pos1.y(), pos1.z()), random, noise);
			}
			if (s2) {
				leavesBall(world, pos.offset(pos2.x(), pos2.y(), pos2.z()), random, noise);
			}
		}
		
		float size1 = size * MHelper.randRange(0.75F, 0.95F, random);
		float size2 = size * MHelper.randRange(0.75F, 0.95F, random);
		float angle1 = angle + (float) Math.PI * 0.5F + MHelper.randRange(-0.1F, 0.1F, random);
		float angle2 = angle + (float) Math.PI * 0.5F + MHelper.randRange(-0.1F, 0.1F, random);
		
		if (s1) {
			branch(pos1.x(), pos1.y(), pos1.z(), size1, angle1, random, depth - 1, world, pos);
		}
		if (s2) {
			branch(pos2.x(), pos2.y(), pos2.z(), size2, angle2, random, depth - 1, world, pos);
		}
	}
	
	private void leavesBall(WorldGenLevel world, BlockPos pos, Random random, OpenSimplexNoise noise) {
		float radius = MHelper.randRange(4.5F, 6.5F, random);
		
		SDF sphere = new SDFSphere().setRadius(radius)
									.setBlock(EndBlocks.PYTHADENDRON_LEAVES.defaultBlockState()
																		   .setValue(LeavesBlock.DISTANCE, 6));
		sphere = new SDFScale3D().setScale(1, 0.6F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return (float) noise.eval(vec.x() * 0.2, vec.y() * 0.2, vec.z() * 0.2) * 3;
		}).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return random.nextFloat() * 3F - 1.5F;
		}).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere)
									 .setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
		MutableBlockPos mut = new MutableBlockPos();
		sphere.addPostProcess((info) -> {
			if (random.nextInt(5) == 0) {
				for (Direction dir : Direction.values()) {
					BlockState state = info.getState(dir, 2);
					if (state.isAir()) {
						return info.getState();
					}
				}
				info.setState(EndBlocks.PYTHADENDRON.getBark().defaultBlockState());
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
		});
		sphere.fillRecursiveIgnore(world, pos, IGNORE);
	}
	
	static {
		REPLACE = (state) -> {
			/*if (state.is(TagAPI.BLOCK_END_GROUND)) {
				return true;
			}*/
			if (state.getBlock() == EndBlocks.PYTHADENDRON_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return EndBlocks.PYTHADENDRON.isTreeLog(state);
		};
		
		POST = (info) -> {
			if (EndBlocks.PYTHADENDRON.isTreeLog(info.getStateUp()) && EndBlocks.PYTHADENDRON.isTreeLog(info.getStateDown())) {
				return EndBlocks.PYTHADENDRON.getLog().defaultBlockState();
			}
			return info.getState();
		};
	}
}
