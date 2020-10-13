package ru.betterend.world.features;

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
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class PythadendronTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()).getBlock() != BlockRegistry.CHORUS_NYLIUM) return false;
		
		float size = MHelper.randRange(10, 13, random);
		List<List<Vector3f>> splines = Lists.newArrayList();
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 4);
		SplineHelper.offsetParts(spline, random, 0.7F, 0, 0.7F);
		Vector3f last = spline.get(spline.size() - 1);
		List<Vector3f> ends = Lists.newArrayList();
		branch(splines, ends, last.getX(), last.getY(), last.getZ(), size * 0.6F, MHelper.randRange(0, MHelper.PI2, random), random, MHelper.floor(Math.log(size) * 1.5F));
		
		SDF function = SplineHelper.buildSDF(spline, 1.4F, 0.8F, (bpos) -> {
			return BlockRegistry.PYTHADENDRON.bark.getDefaultState();
		});
		
		for (List<Vector3f> sp: splines) {
			float width = 0.8F - (sp.get(0).getY() - size) / 40;
			SDF funcSp = SplineHelper.buildSDF(sp, width, width, (bpos) -> {
				return BlockRegistry.PYTHADENDRON.bark.getDefaultState();
			});
			function = new SDFUnion().setSourceA(function).setSourceB(funcSp);
		}
		
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		for (Vector3f v: ends) {
			SDF sphere = new SDFSphere().setRadius(MHelper.randRange(2.5F, 3.5F, random)).setBlock(BlockRegistry.PYTHADENDRON_LEAVES);
			sphere = new SDFTranslate().setTranslate(v.getX(), v.getY(), v.getZ()).setSource(sphere);
			sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2); }).setSource(sphere);
			function = new SDFUnion().setSourceA(function).setSourceB(sphere);
		}
		
		function = new SDFScale().setScale(MHelper.randRange(1.6F, 2.0F, random)).setSource(function);
		function.setReplaceFunction(REPLACE);
		function.fillRecursive(world, pos);
		
		return true;
	}
	
	private void branch(List<List<Vector3f>> splines, List<Vector3f> ends, float x, float y, float z, float size, float angle, Random random, int depth) {
		if (depth == 0) return;
		
		float dx = (float) Math.cos(angle) * size * 0.3F;
		float dz = (float) Math.sin(angle) * size * 0.3F;
		
		float x1 = x + dx;
		float z1 = z + dz;
		float x2 = x - dx;
		float z2 = z - dz;
		
		List<Vector3f> spline = SplineHelper.makeSpline(x, y, z, x1, y, z1, 5);
		SplineHelper.parableOffset(spline, size);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		splines.add(spline);
		Vector3f pos1 = spline.get(spline.size() - 1);
		
		spline = SplineHelper.makeSpline(x, y, z, x2, y, z2, 5);
		SplineHelper.parableOffset(spline, size);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		splines.add(spline);
		Vector3f pos2 = spline.get(spline.size() - 1);
		
		if (depth == 1) {
			ends.add(pos1);
			ends.add(pos2);
		}
		
		branch(splines, ends, pos1.getX(), pos1.getY(), pos1.getZ(), size * 0.8F, angle + (float) Math.PI * 0.5F, random, depth - 1);
		branch(splines, ends, pos2.getX(), pos2.getY(), pos2.getZ(), size * 0.8F, angle + (float) Math.PI * 0.5F, random, depth - 1);
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(BlockTagRegistry.END_GROUND)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
