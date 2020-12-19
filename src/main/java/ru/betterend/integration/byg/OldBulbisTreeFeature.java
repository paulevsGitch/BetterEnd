package ru.betterend.integration.byg;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class OldBulbisTreeFeature extends DefaultFeature {
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		BlockState stem = Integrations.BYG.getDefaultState("bulbis_stem");
		BlockState wood = Integrations.BYG.getDefaultState("bulbis_wood");
		BlockState cap = Integrations.BYG.getDefaultState(random.nextBoolean() ? "bulbis_shell" : "purple_bulbis_shell");
		
		float size = MHelper.randRange(10, 20, random);
		int count = (int) (size * 0.45F);
		float var = MHelper.PI2 /  (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		SDF sdf = null;
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			SplineHelper.scale(spline, size + MHelper.randRange(0, size * 0.5F, random));
			SplineHelper.offset(spline, new Vector3f((20 - size) * 0.6F, 0, 0));
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
			SDF branch = SplineHelper.buildSDF(spline, 1.3F, 0.8F, (bpos) -> {
				return stem;
			});
			Vector3f last = spline.get(spline.size() - 1);
			SDF sphere = new SDFSphere().setRadius((size + MHelper.randRange(0, size * 0.5F, random)) * 0.2F).setBlock(cap);
			sphere = new SDFTranslate().setTranslate(last.getX(), last.getY(), last.getZ()).setSource(sphere);
			branch = new SDFUnion().setSourceA(branch).setSourceB(sphere);
			sdf = (sdf == null) ? branch : new SDFUnion().setSourceA(sdf).setSourceB(branch);
		}
		
		Function<BlockState, Boolean> replacement = (state) -> {
			if (state.equals(stem) || state.equals(wood) || state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		sdf.setReplaceFunction(replacement).setPostProcess((info) -> {
			if (info.getState().equals(stem) && (!info.getStateUp().equals(stem) || !info.getStateDown().equals(stem))) {
				return wood;
			}
			return info.getState();
		}).fillRecursive(world, pos);
		makeRoots(world, pos, size * 0.4F + 5, random, wood, replacement);

		return true;
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
			new Vector3f(0.1F, 0.7F, 0),
			new Vector3f(0.3F, 0.3F, 0),
			new Vector3f(0.7F, 0.05F, 0),
			new Vector3f(0.8F, -0.2F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
	}
}
