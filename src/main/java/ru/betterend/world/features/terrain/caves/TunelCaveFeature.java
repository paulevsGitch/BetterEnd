package ru.betterend.world.features.terrain.caves;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;

public class TunelCaveFeature extends EndCaveFeature {
	private static final Function<BlockState, Boolean> REPLACE;

	@Override
	protected Set<BlockPos> place(WorldGenLevel world, BlockPos center, int radius, Random random) {
		// OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534,
		// center.getX(), center.getZ()));
		float rad = radius * 0.15F;
		int min = Mth.ceil(rad) - 15;
		int max = 31 - Mth.floor(rad);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, 0, 0, radius / 3);
		spline = SplineHelper.smoothSpline(spline, 5);
		SplineHelper.offsetParts(spline, random, 5, radius * 0.4F, 5);
		for (Vector3f vec : spline) {
			float x = Mth.clamp(vec.x(), min, max);
			float y = Mth.clamp(vec.y(), -radius, radius);
			float z = Mth.clamp(vec.z(), min, max);
			vec.set(x, y, z);
		}
		SDF sdf = SplineHelper.buildSDF(spline, rad, rad, (vec) -> Blocks.AIR.defaultBlockState());
		Set<BlockPos> positions = sdf.setReplaceFunction(REPLACE).getPositions(world, center);
		for (BlockPos p : positions) {
			BlocksHelper.setWithoutUpdate(world, p, CAVE_AIR);
		}
		return positions;
	}

	static {
		REPLACE = (state) -> {
			return state.isIn(EndTags.GEN_TERRAIN) || state.getMaterial().isReplaceable()
					|| state.getMaterial().equals(Material.PLANT) || state.getMaterial().equals(Material.LEAVES);
		};
	}
}
