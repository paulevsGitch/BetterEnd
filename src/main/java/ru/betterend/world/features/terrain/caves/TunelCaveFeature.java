package ru.betterend.world.features.terrain.caves;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;

public class TunelCaveFeature extends EndCaveFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	protected Set<BlockPos> generate(StructureWorldAccess world, BlockPos center, int radius, Random random) {
		//OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
		float rad = radius * 0.15F;
		int min = MathHelper.ceil(rad) - 15;
		int max = 31 - MathHelper.floor(rad);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, 0, 0, radius / 3);
		spline = SplineHelper.smoothSpline(spline, 5);
		SplineHelper.offsetParts(spline, random, 5, radius * 0.4F, 5);
		for (Vector3f vec: spline) {
			float x = MathHelper.clamp(vec.getX(), min, max);
			float y = MathHelper.clamp(vec.getY(), -radius, radius);
			float z = MathHelper.clamp(vec.getZ(), min, max);
			vec.set(x, y, z);
		}
		SDF sdf = SplineHelper.buildSDF(spline, rad, rad, (vec) -> Blocks.AIR.getDefaultState());
		Set<BlockPos> positions = sdf.setReplaceFunction(REPLACE).getPositions(world, center);
		for (BlockPos p: positions) {
			BlocksHelper.setWithoutUpdate(world, p, CAVE_AIR);
		}
		return positions;
	}
	
	static {
		REPLACE = (state) -> {
			return state.isIn(EndTags.GEN_TERRAIN)
					|| state.getMaterial().isReplaceable()
					|| state.getMaterial().equals(Material.PLANT)
					|| state.getMaterial().equals(Material.LEAVES);
		};
	}
}
