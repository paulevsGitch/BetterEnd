package ru.betterend.integration.byg.features;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.world.features.DefaultFeature;

public class BigEtherTreeFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).getBlock().is(EndTags.END_GROUND))
			return false;

		BlockState log = Integrations.BYG.getDefaultState("ether_log");
		BlockState wood = Integrations.BYG.getDefaultState("ether_wood");
		// BlockState leaves = Integrations.BYG.getDefaultState("ether_leaves");
		Function<BlockPos, BlockState> splinePlacer = (bpos) -> {
			return log;
		};
		Function<BlockState, Boolean> replace = (state) -> {
			return state.is(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT)
					|| state.getMaterial().isReplaceable();
		};

		int height = MHelper.randRange(40, 60, random);
		List<Vector3f> trunk = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, height / 4);
		SplineHelper.offsetParts(trunk, random, 2F, 0, 2F);
		SDF sdf = SplineHelper.buildSDF(trunk, 2.3F, 0.8F, splinePlacer);

		int count = height / 15;
		for (int i = 1; i < count; i++) {
			float splinePos = (float) i / (float) count;
			float startAngle = random.nextFloat() * MHelper.PI2;
			float length = (1 - splinePos) * height * 0.4F;
			int points = (int) (length / 3);
			List<Vector3f> branch = SplineHelper.makeSpline(0, 0, 0, length, 0, 0, points < 2 ? 2 : points);
			SplineHelper.powerOffset(branch, length, 2F);
			int rotCount = MHelper.randRange(5, 7, random);
			// float startRad = MathHelper.lerp(splinePos, 2.3F, 0.8F) * 0.8F;
			Vector3f start = SplineHelper.getPos(trunk, splinePos * (trunk.size() - 1));
			for (int j = 0; j < rotCount; j++) {
				float angle = startAngle + (float) j / rotCount * MHelper.PI2;
				List<Vector3f> br = SplineHelper.copySpline(branch);
				SplineHelper.offsetParts(br, random, 0, 1, 1);
				SplineHelper.rotateSpline(br, angle);

				SplineHelper.offset(br, start);
				SplineHelper.fillSpline(br, world, wood, pos, replace);
			}
		}

		sdf.setReplaceFunction((state) -> {
			return state.is(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT)
					|| state.getMaterial().isReplaceable();
		}).addPostProcess((info) -> {
			if (info.getState().equals(log) && (!info.getStateUp().equals(log) || !info.getStateDown().equals(log))) {
				return wood;
			}
			return info.getState();
		}).fillRecursive(world, pos);

		return true;
	}

	// private void makeLeavesSphere(StructureWorldAccess world, BlockPos pos,
	// BlockState leaves, Function<BlockState, Boolean> ignore) {
	//
	// }
}
