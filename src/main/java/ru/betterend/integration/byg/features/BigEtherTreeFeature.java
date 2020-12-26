package ru.betterend.integration.byg.features;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;

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
import ru.betterend.world.features.DefaultFeature;

public class BigEtherTreeFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		BlockState log = Integrations.BYG.getDefaultState("ether_log");
		BlockState wood = Integrations.BYG.getDefaultState("ether_wood");
		BlockState leaves = Integrations.BYG.getDefaultState("ether_leaves");
		
		int height = MHelper.randRange(20, 30, random);
		List<Vector3f> trunk = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, height / 2);
		SplineHelper.offsetParts(trunk, random, 0.5F, 0, 0.5F);
		SDF sdf = SplineHelper.buildSDF(trunk, 2.3F, 0.8F, (bpos) -> { return log;});
		sdf.setReplaceFunction((state) -> {
			return state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT) || state.getMaterial().isReplaceable();
		}).fillRecursive(world, pos);

		return true;
	}
	
	private void makeLeavesSphere(StructureWorldAccess world, BlockPos pos, BlockState leaves, Function<BlockState, Boolean> ignore) {
		
	}
}
