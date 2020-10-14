package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;

public class PythadendronTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()).getBlock() != BlockRegistry.CHORUS_NYLIUM) return false;
		
		float size = MHelper.randRange(10, 20, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 4);
		SplineHelper.offsetParts(spline, random, 0.7F, 0, 0.7F);
		Vector3f last = spline.get(spline.size() - 1);
		
		branch(last.getX(), last.getY(), last.getZ(), size * 1.5F, MHelper.randRange(0, MHelper.PI2, random), random, 3, world, pos);
		
		SDF function = SplineHelper.buildSDF(spline, 1.5F, 0.8F, (bpos) -> {
			return BlockRegistry.PYTHADENDRON.bark.getDefaultState();
		});
		
		function.setReplaceFunction(REPLACE);
		function.fillRecursive(world, pos);
		
		return true;
	}
	
	private void branch(float x, float y, float z, float size, float angle, Random random, int depth, StructureWorldAccess world, BlockPos pos) {
		if (depth == 0) return;
		
		float dx = (float) Math.cos(angle) * size * 0.2F;
		float dz = (float) Math.sin(angle) * size * 0.2F;
		
		float x1 = x + dx;
		float z1 = z + dz;
		float x2 = x - dx;
		float z2 = z - dz;
		
		List<Vector3f> spline = SplineHelper.makeSpline(x, y, z, x1, y, z1, 5);
		SplineHelper.powerOffset(spline, size * MHelper.randRange(1.0F, 2.0F, random), 4);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		Vector3f pos1 = spline.get(spline.size() - 1);
		
		SplineHelper.fillSpline(spline, world, BlockRegistry.PYTHADENDRON.bark.getDefaultState(), pos, REPLACE);
		
		spline = SplineHelper.makeSpline(x, y, z, x2, y, z2, 5);
		SplineHelper.powerOffset(spline, size * MHelper.randRange(1.0F, 2.0F, random), 4);
		SplineHelper.offsetParts(spline, random, 0.3F, 0, 0.3F);
		Vector3f pos2 = spline.get(spline.size() - 1);
		
		SplineHelper.fillSpline(spline, world, BlockRegistry.PYTHADENDRON.bark.getDefaultState(), pos, REPLACE);
		
		float size1 = size * MHelper.randRange(0.75F, 0.95F, random);
		float size2 = size * MHelper.randRange(0.75F, 0.95F, random);
		float angle1 = angle + (float) Math.PI * 0.5F + MHelper.randRange(-0.1F, 0.1F, random);
		float angle2 = angle + (float) Math.PI * 0.5F + MHelper.randRange(-0.1F, 0.1F, random);
		branch(pos1.getX(), pos1.getY(), pos1.getZ(), size1, angle1, random, depth - 1, world, pos);
		branch(pos2.getX(), pos2.getY(), pos2.getZ(), size2, angle2, random, depth - 1, world, pos);
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
