package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class CapsacisTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final List<Vector3f> ROOT;
	
	private final Function<Random, BlockState> capFunction;
	
	public CapsacisTreeFeature(Function<Random, BlockState> capFunction) {
		this.capFunction = capFunction;
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		int height = MHelper.randRange(20, 30, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, height * 0.2F, 0, 5);
		
		BlockPos center = pos.up(9);
		if (!SplineHelper.canGenerate(spline, center, world, REPLACE)) {
			return false;
		}
		
		BlockState woodState = EndBlocks.CAPSACIS.bark.getDefaultState();
		BlockState logState = EndBlocks.CAPSACIS.log.getDefaultState();
		BlockState capState = capFunction.apply(random);
		
		SplineHelper.offsetParts(spline, random, 0.5F, 0, 0.5F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.5F, 0.8F, (bpos) -> {
			return woodState;
		});
		
		SDF cap = makeCap(height * 0.2F, height * 0.1F, random, capState);
		
		sdf = new SDFUnion().setSourceA(sdf).setSourceB(cap);
		makeRoots(world, center, height * 0.4F, random, woodState);
		sdf.addPostProcess((info) -> {
			if (EndBlocks.CAPSACIS.isTreeLog(info.getStateUp()) && EndBlocks.CAPSACIS.isTreeLog(info.getStateDown())) {
				return logState;
			}
			return info.getState();
		}).fillRecursive(world, center);
		
		return true;
	}
	
	private SDF makeCap(float offset, float radius, Random random, BlockState capState) {
		float angle = random.nextFloat() * MHelper.PI2;
		int count = MHelper.randRange(5, 7, random);
		SDF cap = new SDFSphere().setRadius(radius).setBlock(capState);
		SDF cone = new SDFCappedCone().setRadius1(radius).setRadius2(radius * 0.4F).setHeight(radius * 0.3F).setBlock(capState);
		cone = new SDFTranslate().setTranslate(0, radius * 0.3F, 0).setSource(cone);
		cap = new SDFSmoothUnion().setRadius(5).setSourceA(cap).setSourceB(cone);
		SDF upperSphere = new SDFSphere().setRadius(radius * 0.4F).setBlock(capState);
		upperSphere = new SDFTranslate().setTranslate(0, radius * 0.3F, 0).setSource(upperSphere);
		cap = new SDFSmoothUnion().setRadius(5).setSourceA(cap).setSourceB(upperSphere);
		cap = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(1F).setSource(cap);
		
		cap = new SDFTranslate().setTranslate(0, offset, 0).setSource(cap);
		
		return cap;
	}
	
	private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random, BlockState state) {
		int count = (int) (radius);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			SplineHelper.offsetParts(branch, random, 0.5F, 0.7F, 0.5F);
			Vector3f last = branch.get(branch.size() - 1);
			if (world.getBlockState(pos.add(last.getX(), last.getY(), last.getZ())).isIn(EndTags.GEN_TERRAIN)) {
				SplineHelper.fillSpline(branch, world, state, pos, REPLACE);
			}
		}
	}
	
	static {
		REPLACE = (state) -> {
			return EndBlocks.CAPSACIS.isTreeLog(state) || state.getMaterial().isReplaceable();
		};
		
		ROOT = Lists.newArrayList(
			new Vector3f(0F, 1F, 0),
			new Vector3f(0.1F, 0.7F, 0),
			new Vector3f(0.3F, 0.3F, 0),
			new Vector3f(0.7F, 0.05F, 0),
			new Vector3f(0.8F, -0.2F, 0)
		);
		SplineHelper.offset(ROOT, Vector3f.NEGATIVE_Y);
	}
}
