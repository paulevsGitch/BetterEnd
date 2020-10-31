package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class LacugroveFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final Function<PosInfo, BlockState> POST;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) return false;
		
		float size = MHelper.randRange(15, 25, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		
		if (!SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
			return false;
		}
		
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		
		float radius = MHelper.randRange(6F, 8F, random);
		radius *= (size - 15F) / 20F + 1F;
		Vector3f center = spline.get(4);
		leavesBall(world, pos.add(center.getX(), center.getY(), center.getZ()), radius, random, noise);
		
		radius = MHelper.randRange(1.2F, 1.8F, random);
		SDF function = SplineHelper.buildSDF(spline, radius, 0.7F, (bpos) -> {
			return EndBlocks.LACUGROVE.bark.getDefaultState();
		});
		
		function.setReplaceFunction(REPLACE);
		function.setPostProcess(POST);
		function.fillRecursive(world, pos);
		
		spline = spline.subList(4, 6);
		SplineHelper.fillSpline(spline, world, EndBlocks.LACUGROVE.bark.getDefaultState(), pos, REPLACE);
		
		Mutable mut = new Mutable();
		int offset = random.nextInt(2);
		for (int i = 0; i < 100; i++) {
			double px = pos.getX() + MHelper.randRange(-5, 5, random);
			double pz = pos.getZ() + MHelper.randRange(-5, 5, random);
			mut.setX(MHelper.floor(px + 0.5));
			mut.setZ(MHelper.floor(pz + 0.5));
			if (((mut.getX() + mut.getZ() + offset) & 1) == 0) {
				double distance = 3.5 - MHelper.length(px - pos.getX(), pz - pos.getZ()) * 0.5;
				if (distance > 0) {
					int minY = MHelper.floor(pos.getY() - distance * 0.5);
					int maxY = MHelper.floor(pos.getY() + distance + random.nextDouble());
					boolean generate = false;
					for (int y = minY; y < maxY; y++) {
						mut.setY(y);
						if (world.getBlockState(mut).isIn(EndTags.END_GROUND)) {
							generate = true;
							break;
						}
					}
					if (generate) {
						int top = maxY - 1;
						for (int y = top; y >= minY; y--) {
							mut.setY(y);
							BlockState state = world.getBlockState(mut);
							if (state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT) || state.isIn(EndTags.END_GROUND)) {
								BlocksHelper.setWithoutUpdate(world, mut, y == top ? EndBlocks.LACUGROVE.bark : EndBlocks.LACUGROVE.log);
							}
							else {
								break;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void leavesBall(StructureWorldAccess world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EndBlocks.LACUGROVE_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1));
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 3; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return random.nextFloat() * 3F - 1.5F; }).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(new SDFTranslate().setTranslate(0, -radius - 2, 0).setSource(sphere));
		sphere.fillRecursiveIgnore(world, pos, IGNORE);
		//sphere.fillArea(world, pos, new Box(pos).expand(radius));
		
		if (radius > 5) {
			int count = (int) (radius * 2.5F);
			for (int i = 0; i < count; i++) {
				BlockPos p = pos.add(random.nextGaussian() * 1, random.nextGaussian() * 1, random.nextGaussian() * 1);
				boolean place = true;
				for (Direction d: Direction.values()) {
					BlockState state = world.getBlockState(p.offset(d));
					if (!EndBlocks.LACUGROVE.isTreeLog(state) && !state.isOf(EndBlocks.LACUGROVE_LEAVES)) {
						place = false;
						break;
					}
				}
				if (place) {
					BlocksHelper.setWithoutUpdate(world, p, EndBlocks.LACUGROVE.bark);
				}
			}
		}
		
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.LACUGROVE.bark);
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (EndBlocks.LACUGROVE.isTreeLog(state)) {
				return true;
			}
			if (state.getBlock() == EndBlocks.LACUGROVE_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return EndBlocks.LACUGROVE.isTreeLog(state);
		};
		
		POST = (info) -> {
			if (EndBlocks.LACUGROVE.isTreeLog(info.getStateUp()) && EndBlocks.LACUGROVE.isTreeLog(info.getStateDown())) {
				return EndBlocks.LACUGROVE.log.getDefaultState();
			}
			return info.getState();
		};
	}
}
