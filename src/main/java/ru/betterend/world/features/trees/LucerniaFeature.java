package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.world.features.DefaultFeature;

public class LucerniaFeature extends DefaultFeature {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).getBlock().is(EndTags.END_GROUND))
			return false;

		float size = MHelper.randRange(12, 20, random);
		int count = (int) (size * 0.3F);
		float var = MHelper.PI2 / (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.scale(spline, size * MHelper.randRange(0.5F, 1F, random));
			SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
			SplineHelper.fillSpline(spline, world, EndBlocks.LUCERNIA.bark.defaultBlockState(), pos, REPLACE);
			Vector3f last = spline.get(spline.size() - 1);
			float leavesRadius = (size * 0.13F + MHelper.randRange(0.8F, 1.5F, random)) * 1.4F;
			OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
			leavesBall(world, pos.offset(last.x(), last.y(), last.z()), leavesRadius, random, noise, config != null);
		}

		makeRoots(world, pos.offset(0, MHelper.randRange(3, 5, random), 0), size * 0.35F, random);

		return true;
	}

	private void leavesBall(WorldGenLevel world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise,
			boolean natural) {
		SDF sphere = new SDFSphere().setRadius(radius)
				.setBlock(EndBlocks.LUCERNIA_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 6));
		SDF sub = new SDFScale().setScale(5).setSource(sphere);
		sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return (float) noise.eval(vec.x() * 0.2, vec.y() * 0.2, vec.z() * 0.2) * 2F;
		}).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return MHelper.randRange(-1.5F, 1.5F, random);
		}).setSource(sphere);

		MutableBlockPos mut = new MutableBlockPos();
		for (Direction d1 : BlocksHelper.HORIZONTAL) {
			BlockPos p = mut.set(pos).move(Direction.UP).move(d1).immutable();
			BlocksHelper.setWithoutUpdate(world, p, EndBlocks.LUCERNIA.bark.defaultBlockState());
			for (Direction d2 : BlocksHelper.HORIZONTAL) {
				mut.set(p).move(Direction.UP).move(d2);
				BlocksHelper.setWithoutUpdate(world, p, EndBlocks.LUCERNIA.bark.defaultBlockState());
			}
		}

		BlockState top = EndBlocks.FILALUX.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP);
		BlockState middle = EndBlocks.FILALUX.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE,
				TripleShape.MIDDLE);
		BlockState bottom = EndBlocks.FILALUX.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE,
				TripleShape.BOTTOM);
		BlockState outer = EndBlocks.LUCERNIA_OUTER_LEAVES.defaultBlockState();

		List<BlockPos> support = Lists.newArrayList();
		sphere.addPostProcess((info) -> {
			if (natural && random.nextInt(6) == 0 && info.getStateDown().isAir()) {
				BlockPos d = info.getPos().below();
				support.add(d);
			}
			if (random.nextInt(15) == 0) {
				for (Direction dir : Direction.values()) {
					BlockState state = info.getState(dir, 2);
					if (state.isAir()) {
						return info.getState();
					}
				}
				info.setState(EndBlocks.LUCERNIA.bark.defaultBlockState());
			}

			MHelper.shuffle(DIRECTIONS, random);
			for (Direction d : DIRECTIONS) {
				if (info.getState(d).isAir()) {
					info.setBlockPos(info.getPos().relative(d), outer.setValue(FurBlock.FACING, d));
				}
			}

			if (EndBlocks.LUCERNIA.isTreeLog(info.getState())) {
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
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.LUCERNIA.bark);

		support.forEach((bpos) -> {
			BlockState state = world.getBlockState(bpos);
			if (state.isAir() || state.is(EndBlocks.LUCERNIA_OUTER_LEAVES)) {
				int count = MHelper.randRange(3, 8, random);
				mut.set(bpos);
				if (world.getBlockState(mut.above()).is(EndBlocks.LUCERNIA_LEAVES)) {
					BlocksHelper.setWithoutUpdate(world, mut, top);
					for (int i = 1; i < count; i++) {
						mut.setY(mut.getY() - 1);
						if (world.isEmptyBlock(mut.below())) {
							BlocksHelper.setWithoutUpdate(world, mut, middle);
						} else {
							break;
						}
					}
					BlocksHelper.setWithoutUpdate(world, mut, bottom);
				}
			}
		});
	}

	private void makeRoots(WorldGenLevel world, BlockPos pos, float radius, Random random) {
		int count = (int) (radius * 1.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);

			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (world.getBlockState(pos.offset(last.x(), last.y(), last.z())).is(EndTags.GEN_TERRAIN)) {
				SplineHelper.fillSplineForce(branch, world, EndBlocks.LUCERNIA.bark.defaultBlockState(), pos, REPLACE);
			}
		}
	}

	static {
		REPLACE = (state) -> {
			if (state.is(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() == EndBlocks.LUCERNIA_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};

		IGNORE = (state) -> {
			return EndBlocks.LUCERNIA.isTreeLog(state);
		};

		SPLINE = Lists.newArrayList(new Vector3f(0.00F, 0.00F, 0.00F), new Vector3f(0.10F, 0.35F, 0.00F),
				new Vector3f(0.20F, 0.50F, 0.00F), new Vector3f(0.30F, 0.55F, 0.00F), new Vector3f(0.42F, 0.70F, 0.00F),
				new Vector3f(0.50F, 1.00F, 0.00F));

		ROOT = Lists.newArrayList(new Vector3f(0.1F, 0.70F, 0), new Vector3f(0.3F, 0.30F, 0),
				new Vector3f(0.7F, 0.05F, 0), new Vector3f(0.8F, -0.20F, 0));
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
	}
}
