package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.PosInfo;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class DragonTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final Function<PosInfo, BlockState> POST;
	private static final List<Vector3f> BRANCH;
	private static final List<Vector3f> SIDE1;
	private static final List<Vector3f> SIDE2;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		float size = MHelper.randRange(10, 25, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		
		if (!SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
			return false;
		}
		BlocksHelper.setWithoutUpdate(world, pos, AIR);
		
		Vector3f last = SplineHelper.getPos(spline, 3.5F);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		float radius = size * MHelper.randRange(0.5F, 0.7F, random);
		makeCap(world, pos.add(last.getX(), last.getY(), last.getZ()), radius, random, noise);
		
		last = spline.get(0);
		makeRoots(world, pos.add(last.getX(), last.getY(), last.getZ()), radius, random);
		
		radius = MHelper.randRange(1.2F, 2.3F, random);
		SDF function = SplineHelper.buildSDF(spline, radius, 1.2F, (bpos) -> {
			return EndBlocks.DRAGON_TREE.bark.getDefaultState();
		});
		
		function.setReplaceFunction(REPLACE);
		function.setPostProcess(POST);
		function.fillRecursiveIgnore(world, pos, IGNORE);
		
		return true;
	}
	
	private void makeCap(StructureWorldAccess world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise) {
		int count = (int) radius;
		int offset = (int) (BRANCH.get(BRANCH.size() - 1).getY() * radius);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(BRANCH);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			SplineHelper.fillSpline(branch, world, EndBlocks.DRAGON_TREE.bark.getDefaultState(), pos, REPLACE);
			
			branch = SplineHelper.copySpline(SIDE1);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			SplineHelper.fillSpline(branch, world, EndBlocks.DRAGON_TREE.bark.getDefaultState(), pos, REPLACE);
			
			branch = SplineHelper.copySpline(SIDE2);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			SplineHelper.fillSpline(branch, world, EndBlocks.DRAGON_TREE.bark.getDefaultState(), pos, REPLACE);
		}
		leavesBall(world, pos.up(offset), radius * 1.15F + 2, random, noise);
	}
	
	private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random) {
		int count = (int) (radius * 1.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			SplineHelper.fillSplineRoot(branch, world, EndBlocks.DRAGON_TREE.bark.getDefaultState(), pos, REPLACE);
		}
	}
	
	private void leavesBall(StructureWorldAccess world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EndBlocks.DRAGON_TREE_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 6));
		SDF sub = new SDFScale().setScale(5).setSource(sphere);
		sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 1.5F; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return random.nextFloat() * 3F - 1.5F; }).setSource(sphere);
		Mutable mut = new Mutable();
		sphere.setPostProcess((info) -> {
			if (random.nextInt(5) == 0) {
				for (Direction dir: Direction.values()) {
					BlockState state = info.getState(dir, 2);
					if (state.isAir()) {
						return info.getState();
					}
				}
				info.setState(EndBlocks.DRAGON_TREE.bark.getDefaultState());
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
									int distance = state.get(LeavesBlock.DISTANCE);
									if (d < distance) {
										info.setState(mut, state.with(LeavesBlock.DISTANCE, d));
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
		/*Collection<PosInfo> list = sphere.fillRecursiveIgnore(world, pos, IGNORE);
		list.forEach((info) -> {
			if (info.getState().getBlock() instanceof LeavesBlock && info.getState().get(LeavesBlock.DISTANCE) == 6) {
				BlockState state = BlocksHelper.getLeavesState(info.getState(), world, info.getPos());
				if (!state.equals(info.getState())) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), state);
				}
			}
		});*/
		
		
		if (radius > 5) {
			int count = (int) (radius * 2.5F);
			for (int i = 0; i < count; i++) {
				BlockPos p = pos.add(random.nextGaussian() * 1, random.nextGaussian() * 1, random.nextGaussian() * 1);
				boolean place = true;
				for (Direction d: Direction.values()) {
					BlockState state = world.getBlockState(p.offset(d));
					if (!EndBlocks.DRAGON_TREE.isTreeLog(state) && !state.isOf(EndBlocks.DRAGON_TREE_LEAVES)) {
						place = false;
						break;
					}
				}
				if (place) {
					BlocksHelper.setWithoutUpdate(world, p, EndBlocks.DRAGON_TREE.bark);
				}
			}
		}
		
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.DRAGON_TREE.bark);
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() == EndBlocks.DRAGON_TREE_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return EndBlocks.DRAGON_TREE.isTreeLog(state);
		};
		
		POST = (info) -> {
			if (EndBlocks.DRAGON_TREE.isTreeLog(info.getStateUp()) && EndBlocks.DRAGON_TREE.isTreeLog(info.getStateDown())) {
				return EndBlocks.DRAGON_TREE.log.getDefaultState();
			}
			return info.getState();
		};
		
		BRANCH = Lists.newArrayList(new Vector3f(0, 0, 0),
				new Vector3f(0.1F, 0.3F, 0),
				new Vector3f(0.4F, 0.6F, 0),
				new Vector3f(0.8F, 0.8F, 0),
				new Vector3f(1, 1, 0));
		SIDE1 = Lists.newArrayList(new Vector3f(0.4F, 0.6F, 0),
				new Vector3f(0.8F, 0.8F, 0),
				new Vector3f(1, 1, 0));
		SIDE2 = SplineHelper.copySpline(SIDE1);
		
		Vector3f offset1 = new Vector3f(-0.4F, -0.6F, 0);
		Vector3f offset2 = new Vector3f(0.4F, 0.6F, 0);
		
		SplineHelper.offset(SIDE1, offset1);
		SplineHelper.offset(SIDE2, offset1);
		SplineHelper.rotateSpline(SIDE1, 0.5F);
		SplineHelper.rotateSpline(SIDE2, -0.5F);
		SplineHelper.offset(SIDE1, offset2);
		SplineHelper.offset(SIDE2, offset2);
		
		ROOT = Lists.newArrayList(new Vector3f(0F, 1F, 0),
				new Vector3f(0.1F, 0.7F, 0),
				new Vector3f(0.3F, 0.3F, 0),
				new Vector3f(0.7F, 0.05F, 0),
				new Vector3f(0.8F, -0.2F, 0));
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
	}
}
