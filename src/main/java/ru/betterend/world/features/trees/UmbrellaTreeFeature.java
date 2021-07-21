package ru.betterend.world.features.trees;

import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFFlatWave;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.operator.SDFUnion;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.blocks.UmbrellaTreeClusterBlock;
import ru.betterend.blocks.UmbrellaTreeMembraneBlock;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class UmbrellaTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		final NoneFeatureConfiguration config = featureConfig.config();
		if (!world.getBlockState(pos.below()).is(TagAPI.BLOCK_END_GROUND)) return false;
		
		BlockState wood = EndBlocks.UMBRELLA_TREE.bark.defaultBlockState();
		BlockState membrane = EndBlocks.UMBRELLA_TREE_MEMBRANE.defaultBlockState()
															  .setValue(UmbrellaTreeMembraneBlock.COLOR, 1);
		BlockState center = EndBlocks.UMBRELLA_TREE_MEMBRANE.defaultBlockState()
															.setValue(UmbrellaTreeMembraneBlock.COLOR, 0);
		BlockState fruit = EndBlocks.UMBRELLA_TREE_CLUSTER.defaultBlockState()
														  .setValue(UmbrellaTreeClusterBlock.NATURAL, true);
		
		float size = MHelper.randRange(10, 20, random);
		int count = (int) (size * 0.15F);
		float var = MHelper.PI2 / (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		SDF sdf = null;
		List<Center> centers = Lists.newArrayList();
		
		float scale = 1;
		if (config != null) {
			scale = MHelper.randRange(1F, 1.7F, random);
		}
		
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			float sizeXZ = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.7F;
			SplineHelper.scale(spline, sizeXZ, sizeXZ * MHelper.randRange(1F, 2F, random), sizeXZ);
			// SplineHelper.offset(spline, new Vector3f((20 - size) * 0.2F, 0, 0));
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.offsetParts(spline, random, 0.5F, 0, 0.5F);
			
			if (SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
				float rScale = (scale - 1) * 0.4F + 1;
				SDF branch = SplineHelper.buildSDF(spline, 1.2F * rScale, 0.8F * rScale, (bpos) -> {
					return wood;
				});
				
				Vector3f vec = spline.get(spline.size() - 1);
				float radius = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.4F;
				
				sdf = (sdf == null) ? branch : new SDFUnion().setSourceA(sdf).setSourceB(branch);
				SDF mem = makeMembrane(world, radius, random, membrane, center);
				
				float px = MHelper.floor(vec.x()) + 0.5F;
				float py = MHelper.floor(vec.y()) + 0.5F;
				float pz = MHelper.floor(vec.z()) + 0.5F;
				mem = new SDFTranslate().setTranslate(px, py, pz).setSource(mem);
				sdf = new SDFSmoothUnion().setRadius(2).setSourceA(sdf).setSourceB(mem);
				centers.add(new Center(
					pos.getX() + (double) (px * scale),
					pos.getY() + (double) (py * scale),
					pos.getZ() + (double) (pz * scale),
					radius * scale
				));
				
				vec = spline.get(0);
			}
		}
		
		if (sdf == null) {
			return false;
		}
		
		if (scale > 1) {
			sdf = new SDFScale().setScale(scale).setSource(sdf);
		}
		
		sdf.setReplaceFunction(REPLACE).addPostProcess((info) -> {
			if (EndBlocks.UMBRELLA_TREE.isTreeLog(info.getStateUp()) && EndBlocks.UMBRELLA_TREE.isTreeLog(info.getStateDown())) {
				return EndBlocks.UMBRELLA_TREE.log.defaultBlockState();
			}
			else if (info.getState().equals(membrane)) {
				Center min = centers.get(0);
				double d = Double.MAX_VALUE;
				BlockPos bpos = info.getPos();
				for (Center c : centers) {
					double d2 = c.distance(bpos.getX(), bpos.getZ());
					if (d2 < d) {
						d = d2;
						min = c;
					}
				}
				int color = MHelper.floor(d / min.radius * 7);
				color = Mth.clamp(color, 1, 7);
				return info.getState().setValue(UmbrellaTreeMembraneBlock.COLOR, color);
			}
			return info.getState();
		}).fillRecursive(world, pos);
		makeRoots(world, pos, (size * 0.5F + 3) * scale, random, wood);
		
		for (Center c : centers) {
			if (!world.getBlockState(new BlockPos(c.px, c.py, c.pz)).isAir()) {
				count = MHelper.floor(MHelper.randRange(5F, 10F, random) * scale);
				float startAngle = random.nextFloat() * MHelper.PI2;
				for (int i = 0; i < count; i++) {
					float angle = (float) i / count * MHelper.PI2 + startAngle;
					float dist = MHelper.randRange(1.5F, 2.5F, random) * scale;
					double px = c.px + Math.sin(angle) * dist;
					double pz = c.pz + Math.cos(angle) * dist;
					makeFruits(world, px, c.py - 1, pz, fruit, scale);
				}
			}
		}
		
		return true;
	}
	
	private void makeRoots(WorldGenLevel world, BlockPos pos, float radius, Random random, BlockState wood) {
		int count = (int) (radius * 1.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (world.getBlockState(pos.offset(last.x(), last.y(), last.z())).is(TagAPI.BLOCK_GEN_TERRAIN)) {
				SplineHelper.fillSplineForce(branch, world, wood, pos, REPLACE);
			}
		}
	}
	
	private SDF makeMembrane(WorldGenLevel world, float radius, Random random, BlockState membrane, BlockState center) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(membrane);
		SDF sub = new SDFTranslate().setTranslate(0, -4, 0).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
		sphere = new SDFTranslate().setTranslate(0, 1 - radius * 0.5F, 0).setSource(sphere);
		
		float angle = random.nextFloat() * MHelper.PI2;
		int count = (int) MHelper.randRange(radius, radius * 2, random);
		if (count < 5) {
			count = 5;
		}
		sphere = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(0.6F).setSource(sphere);
		
		SDF cent = new SDFSphere().setRadius(2.5F).setBlock(center);
		sphere = new SDFUnion().setSourceA(sphere).setSourceB(cent);
		
		return sphere;
	}
	
	private void makeFruits(WorldGenLevel world, double px, double py, double pz, BlockState fruit, float scale) {
		MutableBlockPos mut = new MutableBlockPos().set(px, py, pz);
		for (int i = 0; i < 8; i++) {
			mut.move(Direction.DOWN);
			if (world.isEmptyBlock(mut)) {
				BlockState state = world.getBlockState(mut.above());
				if (state.is(EndBlocks.UMBRELLA_TREE_MEMBRANE) && state.getValue(UmbrellaTreeMembraneBlock.COLOR) < 2) {
					BlocksHelper.setWithoutUpdate(world, mut, fruit);
				}
				break;
			}
		}
	}
	
	static {
		SPLINE = Lists.newArrayList(
			new Vector3f(0.00F, 0.00F, 0.00F),
			new Vector3f(0.10F, 0.35F, 0.00F),
			new Vector3f(0.20F, 0.50F, 0.00F),
			new Vector3f(0.30F, 0.55F, 0.00F),
			new Vector3f(0.42F, 0.70F, 0.00F),
			new Vector3f(0.50F, 1.00F, 0.00F)
		);
		
		ROOT = Lists.newArrayList(
			new Vector3f(0.1F, 0.70F, 0),
			new Vector3f(0.3F, 0.30F, 0),
			new Vector3f(0.7F, 0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
		
		REPLACE = (state) -> {
			if (state.is(TagAPI.BLOCK_END_GROUND) || state.getMaterial()
													.equals(Material.PLANT) || state.is(EndBlocks.UMBRELLA_TREE_MEMBRANE)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
	
	private class Center {
		final double px;
		final double py;
		final double pz;
		final float radius;
		
		Center(double x, double y, double z, float radius) {
			this.px = x;
			this.py = y;
			this.pz = z;
			this.radius = radius;
		}
		
		double distance(float x, float z) {
			return MHelper.length(px - x, pz - z);
		}
	}
}
