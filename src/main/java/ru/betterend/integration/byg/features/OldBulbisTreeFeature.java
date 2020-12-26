package ru.betterend.integration.byg.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.integration.Integrations;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class OldBulbisTreeFeature extends DefaultFeature {
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;
	private static final List<Vector3f> LEAF;
	private static final List<Vector3f> SIDE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		if (!world.getBlockState(pos.down(4)).getBlock().isIn(EndTags.GEN_TERRAIN)) return false;
		
		BlockState stem = Integrations.BYG.getDefaultState("bulbis_stem");
		BlockState wood = Integrations.BYG.getDefaultState("bulbis_wood");
		BlockState cap = Integrations.BYG.getDefaultState(random.nextBoolean() ? "bulbis_shell" : "purple_bulbis_shell");
		BlockState glow = Integrations.BYG.getDefaultState("purple_shroomlight");
		
		Function<BlockState, Boolean> replacement = (state) -> {
			if (state.equals(stem) || state.equals(wood) || state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		float size = MHelper.randRange(10, 20, random);
		int count = (int) (size * 0.15F);
		float var = MHelper.PI2 /  (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		SDF sdf = null;
		int x1 = ((pos.getX() >> 4) << 4) - 16;
		int z1 = ((pos.getZ() >> 4) << 4) - 16;
		Box limits = new Box(x1, pos.getY() - 5, z1, x1 + 47, pos.getY() + size * 2, z1 + 47);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			float sizeXZ = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.7F;
			SplineHelper.scale(spline, sizeXZ, sizeXZ * 1.5F + MHelper.randRange(0, size * 0.5F, random), sizeXZ);
			SplineHelper.offset(spline, new Vector3f((20 - size), 0, 0));
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.offsetParts(spline, random, 1F, 0, 1F);//1.3F 0.8F
			SDF branch = SplineHelper.buildSDF(spline, 2.3F, 1.3F, (bpos) -> {
				return stem;
			});

			Vector3f vec = spline.get(spline.size() - 1);
			float radius = (size + MHelper.randRange(0, size * 0.5F, random)) * 0.35F;
			bigSphere(world, pos.add(vec.getX(), vec.getY(), vec.getZ()), radius, cap, glow, wood, replacement, random);
			vec = SplineHelper.getPos(spline, 0.3F);
			makeRoots(world, pos.add(vec.getX(), vec.getY(), vec.getZ()), size * 0.4F + 5, random, wood, replacement);

			sdf = (sdf == null) ? branch : new SDFUnion().setSourceA(sdf).setSourceB(branch);
		}
		
		sdf.setReplaceFunction(replacement).setPostProcess((info) -> {
			if (info.getState().equals(stem) && (!info.getStateUp().equals(stem) || !info.getStateDown().equals(stem))) {
				return wood;
			}
			return info.getState();
		}).fillArea(world, pos, limits);
		

		return true;
	}
	
	private void bigSphere(StructureWorldAccess world, BlockPos pos, float radius, BlockState cap, BlockState glow, BlockState wood, Function<BlockState, Boolean> replacement, Random random) {
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(cap);
		
		SDF sphereInner = new SDFSphere().setRadius(radius * 0.53F).setBlock(Blocks.AIR);
		sphereInner = new SDFDisplacement().setFunction((vec) -> {
			return (float) noise.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
		}).setSource(sphereInner);
		
		SDF sphereGlow = new SDFSphere().setRadius(radius * 0.6F).setBlock(glow);
		sphereGlow = new SDFDisplacement().setFunction((vec) -> {
			return (float) noise.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1) * 2F;
		}).setSource(sphereGlow);
		sphereGlow = new SDFSubtraction().setSourceA(sphereGlow).setSourceB(sphereInner);
		
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sphereGlow);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sphereInner);
		
		float offsetY = radius * 1.7F;
		sphere = new SDFUnion().setSourceA(sphere).setSourceB(sphereGlow);
		sphere = new SDFTranslate().setTranslate(0, offsetY, 0).setSource(sphere);
		
		int leafCount = (int) (radius * 0.5F) + 2;
		for (int i = 0; i < 4; i++) {
			float angle = (float) i / 4 * MHelper.PI2;
			List<Vector3f> spline = SplineHelper.copySpline(LEAF);
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.scale(spline, radius * 1.4F);
			SplineHelper.fillSplineForce(spline, world, wood, pos, replacement);
			
			for (int j = 0; j < leafCount; j++) {
				float delta = ((float) j / (float) (leafCount - 1));
				float scale = (float) Math.sin(delta * Math.PI) * 0.8F + 0.2F;
				float index = MathHelper.lerp(delta, 1F, 3.9F);
				Vector3f point = SplineHelper.getPos(spline, index);
				
				List<Vector3f> side = SplineHelper.copySpline(SIDE);
				SplineHelper.rotateSpline(side, angle);
				SplineHelper.scale(side, scale * radius);
				BlockPos p = pos.add(point.getX() + 0.5F, point.getY() + 0.5F, point.getZ() + 0.5F);
				SplineHelper.fillSplineForce(side, world, wood, p, replacement);
			}
		}
		
		sphere.fillArea(world, pos, new Box(pos.up((int) offsetY)).expand(radius * 1.3F));
	}
	
	private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random, BlockState wood, Function<BlockState, Boolean> replacement) {
		int count = (int) (radius * 1.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (world.getBlockState(pos.add(last.getX(), last.getY(), last.getZ())).isIn(EndTags.GEN_TERRAIN)) {
				SplineHelper.fillSpline(branch, world, wood, pos, replacement);
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
		
		ROOT = Lists.newArrayList(new Vector3f(0F, 1F, 0),
			new Vector3f(0.1F,  0.70F, 0),
			new Vector3f(0.3F,  0.30F, 0),
			new Vector3f(0.7F,  0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
		
		LEAF = Lists.newArrayList(
			new Vector3f(0.00F, 0.0F, 0),
			new Vector3f(0.10F, 0.4F, 0),
			new Vector3f(0.40F, 0.8F, 0),
			new Vector3f(0.75F, 0.9F, 0),
			new Vector3f(1.00F, 0.8F, 0)
		);
		
		SIDE = Lists.newArrayList(
			new Vector3f(0, -0.3F, -0.5F),
			new Vector3f(0, -0.1F, -0.3F),
			new Vector3f(0,  0.0F,  0.0F),
			new Vector3f(0, -0.1F,  0.3F),
			new Vector3f(0, -0.3F,  0.5F)
		);
	}
}
