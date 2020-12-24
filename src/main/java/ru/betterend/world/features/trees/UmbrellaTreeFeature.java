package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockUmbrellaTreeMembrane;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class UmbrellaTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		BlockState wood = EndBlocks.UMBRELLA_TREE.bark.getDefaultState();
		BlockState membrane = EndBlocks.UMBRELLA_TREE_MEMBRANE.getDefaultState().with(BlockUmbrellaTreeMembrane.COLOR, 1);
		BlockState center = EndBlocks.UMBRELLA_TREE_MEMBRANE.getDefaultState().with(BlockUmbrellaTreeMembrane.COLOR, 0);
		BlockState fruit = EndBlocks.GLOWING_PILLAR_LUMINOPHOR.getDefaultState();
		
		float size = MHelper.randRange(10, 20, random);
		int count = (int) (size * 0.15F);
		float var = MHelper.PI2 /  (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		SDF sdf = null;
		List<Center> centers = Lists.newArrayList();
		
		float scale = 1;
		if (config != null) {
			scale = MHelper.randRange(1F, 2F, random);
		}
		
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			float sizeXZ = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.7F;
			SplineHelper.scale(spline, sizeXZ, sizeXZ * MHelper.randRange(1F, 2F, random), sizeXZ);
			//SplineHelper.offset(spline, new Vector3f((20 - size) * 0.2F, 0, 0));
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.offsetParts(spline, random, 0.5F, 0, 0.5F);
			
			if (SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
				SDF branch = SplineHelper.buildSDF(spline, 1.2F * scale, 0.8F * scale, (bpos) -> {
					return wood;
				});
	
				Vector3f vec = spline.get(spline.size() - 1);
				float radius = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.4F;
				
				sdf = (sdf == null) ? branch : new SDFUnion().setSourceA(sdf).setSourceB(branch);
				SDF mem = makeMembrane(world, radius, random, membrane, center);
				
				float px = MHelper.floor(vec.getX()) + 0.5F;
				float py = MHelper.floor(vec.getY()) + 0.5F;
				float pz = MHelper.floor(vec.getZ()) + 0.5F;
				mem = new SDFTranslate().setTranslate(px, py, pz).setSource(mem);
				sdf = new SDFSmoothUnion().setRadius(2).setSourceA(sdf).setSourceB(mem);
				centers.add(new Center(pos.getX() + (double) (px * scale), pos.getY() + (double) (py * scale), pos.getZ() + (double) (pz * scale), radius * scale));
				
				vec = spline.get(0);
			}
		}
		
		if (sdf == null) {
			return false;
		}
		
		if (scale > 1) {
			sdf = new SDFScale().setScale(scale).setSource(sdf);
		}
		
		sdf.setReplaceFunction(REPLACE).setPostProcess((info) -> {
			if (EndBlocks.UMBRELLA_TREE.isTreeLog(info.getStateUp()) && EndBlocks.UMBRELLA_TREE.isTreeLog(info.getStateDown())) {
				return EndBlocks.UMBRELLA_TREE.log.getDefaultState();
			}
			else if (info.getState().equals(membrane)) {
				Center min = centers.get(0);
				double d = Double.MAX_VALUE;
				BlockPos bpos = info.getPos();
				for (Center c: centers) {
					double d2 = c.distance(bpos.getX(), bpos.getZ());
					if (d2 < d) {
						d = d2;
						min = c;
					}
				}
				int color = MHelper.floor(d / min.radius * 7);
				color = MathHelper.clamp(color, 1, 7);
				return info.getState().with(BlockUmbrellaTreeMembrane.COLOR, color);
			}
			return info.getState();
		}).fillRecursive(world, pos);
		
		for (Center c: centers) {
			if (!world.getBlockState(new BlockPos(c.px, c.py, c.pz)).isAir()) {
				count = random.nextInt(4);
				float startAngle = random.nextFloat() * MHelper.PI2;
				for (int i = 0; i < count; i++) {
					float angle = (float) i / count * MHelper.PI2 + startAngle;
					float dist = MHelper.randRange(1.8F, 3.4F, random) * scale;
					double px = c.px + Math.sin(angle) * dist;
					double pz = c.pz + Math.cos(angle) * dist;
					makeFruits(world, px, c.py - 1, pz, random, fruit, scale);
				}
			}
		}
		
		makeRoots(world, pos.add(0, 2, 0), (size * 0.3F + 3) * scale, random, wood);
		
		return true;
	}
	
	private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random, BlockState wood) {
		int count = (int) (radius * 1.5F);
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
	
	private SDF makeMembrane(StructureWorldAccess world, float radius, Random random, BlockState membrane, BlockState center) {
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
	
	private void makeFruits(StructureWorldAccess world, double px, double py, double pz, Random random, BlockState fruit, float scale) {
		Mutable mut = new Mutable();
		int length = MHelper.floor(MHelper.randRange(1F, 3F, random) * scale + 0.5F);
		for (int i = 0; i < length; i++) {
			mut.setY(MHelper.floor(py - i));
			//mut.setX(MHelper.floor(px));
			//mut.setZ(MHelper.floor(pz));
			/*if (world.isAir(mut)) {
				BlocksHelper.setWithoutUpdate(world, mut, fruit);
			}*/
			double radius = (1 - (double) i / length) * 0.5;
			for (int j = 0; j < 2; j++) {
				mut.setX(MHelper.floor(random.nextGaussian() * radius + px + 0.5));
				mut.setZ(MHelper.floor(random.nextGaussian() * radius + pz + 0.5));
				if (world.isAir(mut)) {
					BlocksHelper.setWithoutUpdate(world, mut, fruit);
				}
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
			new Vector3f(0.1F,  0.70F, 0),
			new Vector3f(0.3F,  0.30F, 0),
			new Vector3f(0.7F,  0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
		
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT) || state.isOf(EndBlocks.UMBRELLA_TREE_MEMBRANE)) {
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
