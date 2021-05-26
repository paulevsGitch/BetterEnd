package ru.betterend.world.features.terrain;

import java.util.Random;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.primitive.SDFHexPrism;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.DefaultFeature;

public class BigAuroraCrystalFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		int maxY = pos.getY() + BlocksHelper.upRay(world, pos, 16);
		int minY = pos.getY() - BlocksHelper.downRay(world, pos, 16);

		if (maxY - minY < 10) {
			return false;
		}

		int y = MHelper.randRange(minY, maxY, random);
		pos = new BlockPos(pos.getX(), y, pos.getZ());

		int height = MHelper.randRange(5, 25, random);
		SDF prism = new SDFHexPrism().setHeight(height).setRadius(MHelper.randRange(1.7F, 3F, random))
				.setBlock(EndBlocks.AURORA_CRYSTAL);
		Vector3f vec = MHelper.randomHorizontal(random);
		prism = new SDFRotation().setRotation(vec, random.nextFloat()).setSource(prism);
		prism.setReplaceFunction((bState) -> {
			return bState.getMaterial().isReplaceable() || bState.is(EndTags.GEN_TERRAIN)
					|| bState.getMaterial().equals(Material.PLANT) || bState.getMaterial().equals(Material.LEAVES);
		});
		prism.fillRecursive(world, pos);
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AURORA_CRYSTAL);

		return true;
	}
}
