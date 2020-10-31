package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.PosInfo;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class DragonTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<PosInfo, BlockState> POST;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		float size = MHelper.randRange(15, 35, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		
		if (!SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
			return false;
		}
		
		float radius = MHelper.randRange(6F, 8F, random);
		radius *= (size - 15F) / 20F + 1F;
		Vector3f top = spline.get(spline.size() - 1);
		
		radius = size * MHelper.randRange(0.08F, 0.12F, random);
		float radius2 = size / 15F;
		SDF function = SplineHelper.buildSDF(spline, radius, radius2, (bpos) -> {
			return EndBlocks.DRAGON_TREE.bark.getDefaultState();
		});
		
		int branches = (int) ((size - 10) * 0.25F + 5);
		radius = size * MHelper.randRange(0.4F, 0.6F, random);
		/*for (int i = 0; i < branches; i++) {
			float angle = (float) i / (float) branches * MHelper.PI2;
			float x2 = top.getX() + (float) Math.sin(angle) * radius;
			float z2 = top.getZ() + (float) Math.cos(angle) * radius;
			spline = SplineHelper.makeSpline(top.getX(), top.getY(), top.getZ(), x2, top.getY(), z2, 7);
			SplineHelper.powerOffset(spline, radius * 0.5F, 3);
			SplineHelper.fillSpline(spline, world, EndBlocks.DRAGON_TREE.bark.getDefaultState(), pos, (state) -> {
				return state.getMaterial().isReplaceable() || state.isOf(EndBlocks.DRAGON_TREE_LEAVES);
			});
		}*/
		
		BlockState leaves = EndBlocks.DRAGON_TREE_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
		SDF leafCap = new SDFSphere().setRadius(radius + 3).setBlock(leaves);
		leafCap = new SDFScale3D().setScale(1, 0.25F, 1).setSource(leafCap);
		SDF sub = new SDFSphere().setRadius(radius * 2).setBlock(Blocks.AIR);
		sub = new SDFTranslate().setTranslate(0, radius * 2 - 2F, 0).setSource(sub);
		leafCap = new SDFSubtraction().setSourceA(leafCap).setSourceB(sub);
		
		SDF branch = new SDFSphere().setRadius(1).setBlock(EndBlocks.DRAGON_TREE.bark);
		branch = new SDFScale3D().setScale(1, 2F / radius, 1).setSource(branch);
		branch = new SDFTranslate().setTranslate(0, -radius * 0.25F - 1F, 0).setSource(branch);
		branch = new SDFFlatWave().setRaysCount(branches).setIntensity(radius).setSource(branch);
		branch = new SDFCoordModify().setFunction((bpos) -> {
			float dist = MHelper.length(bpos.getX(), bpos.getZ());
			bpos.set(bpos.getX(), bpos.getY() - dist * 0.1F, bpos.getZ());
		}).setSource(branch);
		SDF center = new SDFSphere().setRadius(3).setBlock(EndBlocks.DRAGON_TREE.bark);
		center = new SDFFlatWave().setRaysCount(branches).setIntensity(3).setSource(center);
		branch = new SDFUnion().setSourceA(branch).setSourceB(center);
		sub = new SDFSphere().setRadius(radius + 1).setBlock(leaves);
		sub = new SDFScale3D().setScale(1, 0.25F, 1).setSource(sub);
		branch = new SDFSubtraction().setSourceA(branch).setSourceB(sub);
		
		leafCap = new SDFUnion().setSourceA(leafCap).setSourceB(branch);
		
		OpenSimplexNoise noise = new OpenSimplexNoise(1234);
		leafCap = new SDFCoordModify().setFunction((bpos) -> {
			float dist = MHelper.length(bpos.getX(), bpos.getZ());
			float y = bpos.getY() + (float) noise.eval(bpos.getX() * 0.1 + pos.getX(), bpos.getZ() * 0.1 + pos.getZ()) * dist * 0.3F + dist * 0.25F;
			bpos.set(bpos.getX(), y, bpos.getZ());
		}).setSource(leafCap);
		
		SDF smallLeaf = leafCap;
		leafCap = new SDFTranslate().setTranslate(top.getX(), top.getY() + radius * 0.25F + 1.5F, top.getZ()).setSource(leafCap);
		function = new SDFUnion().setSourceA(function).setSourceB(leafCap);
		
		/*branches = Math.round((size - 15) * 0.1F);
		if (branches > 0) {
			SDF pie = new SDFPie().setRadius(50).setAngle((float) Math.toRadians(135)).setBlock(leaves);
			smallLeaf = new SDFIntersection().setSourceA(leafCap).setSourceB(pie);
			
			for (int i = 0; i < branches; i++) {
				float indexF = MHelper.randRange(3F, 5F, random);
				Vector3f vec = SplineHelper.getPos(spline, indexF);
				float scale = MHelper.randRange(0.3F, 0.6F, random);
				SDF leaf = new SDFScale().setScale(scale).setSource(smallLeaf);
				leaf = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, MHelper.randRange(0, MHelper.PI2, random)).setSource(leaf);
				leaf = new SDFTranslate().setTranslate(vec.getX(), vec.getY(), vec.getZ()).setSource(leaf);
				function = new SDFUnion().setSourceA(function).setSourceB(leaf);
			}
		}*/
		
		function.setReplaceFunction(REPLACE);
		function.setPostProcess(POST);
		function.fillRecursiveIgnore(world, pos, (state) -> {
			return EndBlocks.DRAGON_TREE.isTreeLog(state) || state.isOf(EndBlocks.DRAGON_TREE_LEAVES);
		});
		
		return true;
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
		
		POST = (info) -> {
			if (EndBlocks.DRAGON_TREE.isTreeLog(info.getStateUp()) && EndBlocks.DRAGON_TREE.isTreeLog(info.getStateDown())) {
				return EndBlocks.DRAGON_TREE.log.getDefaultState();
			}
			return info.getState();
		};
	}
}
