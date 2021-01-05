package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.JellyshroomCapBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class JellyshroomFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		BlockState bark = EndBlocks.JELLYSHROOM.bark.getDefaultState();
		BlockState membrane = EndBlocks.JELLYSHROOM_CAP_PURPLE.getDefaultState();
		
		int height = MHelper.randRange(5, 8, random);
		float radius = height * MHelper.randRange(0.15F, 0.25F, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, -1, 0, 0, height, 0, 3);
		SplineHelper.offsetParts(spline, random, 0.5F, 0, 0.5F);
		SDF sdf = SplineHelper.buildSDF(spline, radius, 0.8F, (bpos) -> {
			return bark;
		});
		
		radius = height * MHelper.randRange(0.7F, 0.9F, random);
		if (radius < 1.5F) {
			radius = 1.5F;
		}
		final float membraneRadius = radius;
		SDF cap = makeCap(membraneRadius, random, membrane);
		final Vector3f last = spline.get(spline.size() - 1);
		cap = new SDFTranslate().setTranslate(last.getX(), last.getY(), last.getZ()).setSource(cap);
		sdf = new SDFSmoothUnion().setRadius(3F).setSourceA(sdf).setSourceB(cap);
		sdf.setReplaceFunction(REPLACE).setPostProcess((info) -> {
			if (EndBlocks.JELLYSHROOM.isTreeLog(info.getState())) {
				if (EndBlocks.JELLYSHROOM.isTreeLog(info.getStateUp()) && EndBlocks.JELLYSHROOM.isTreeLog(info.getStateDown())) {
					return EndBlocks.JELLYSHROOM.log.getDefaultState();
				}
			}
			else if (info.getState().isOf(EndBlocks.JELLYSHROOM_CAP_PURPLE)) {
				float dx = info.getPos().getX() - pos.getX() - last.getX();
				float dz = info.getPos().getZ() - pos.getZ() - last.getZ();
				float distance = MHelper.length(dx, dz) / membraneRadius * 7F;
				int color = MathHelper.clamp(MHelper.floor(distance), 0, 7);
				return info.getState().with(JellyshroomCapBlock.COLOR, color);
			}
			return info.getState();
		}).fillRecursive(world, pos);
		radius = height * 0.5F;
		makeRoots(world, pos.add(0, 2, 0), radius, random, bark);
		
		return true;
	}
	
	private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random, BlockState wood) {
		int count = (int) (radius * 3.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (world.getBlockState(pos.add(last.getX(), last.getY(), last.getZ())).isIn(EndTags.GEN_TERRAIN)) {
				SplineHelper.fillSpline(branch, world, wood, pos, REPLACE);
			}
		}
	}
	
	private SDF makeCap(float radius, Random random, BlockState cap) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(cap);
		SDF sub = new SDFTranslate().setTranslate(0, -4, 0).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
		sphere = new SDFTranslate().setTranslate(0, 1 - radius * 0.5F, 0).setSource(sphere);
		
		float angle = random.nextFloat() * MHelper.PI2;
		int count = (int) MHelper.randRange(radius * 0.5F, radius, random);
		if (count < 3) {
			count = 3;
		}
		sphere = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(0.2F).setSource(sphere);
		
		return sphere;
	}
	
	static {
		ROOT = Lists.newArrayList(
			new Vector3f(0.1F,  0.70F, 0),
			new Vector3f(0.3F,  0.30F, 0),
			new Vector3f(0.7F,  0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
		
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
