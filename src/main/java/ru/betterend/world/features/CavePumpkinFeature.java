package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.registry.EndBlocks;

public class CavePumpkinFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.above()).is(TagAPI.GEN_TERRAIN) || !world.isEmptyBlock(pos)
				|| !world.isEmptyBlock(pos.below())) {
			return false;
		}

		int age = random.nextInt(4);
		BlocksHelper.setWithoutUpdate(world, pos,
				EndBlocks.CAVE_PUMPKIN_SEED.defaultBlockState().setValue(BlockProperties.AGE, age));
		if (age > 1) {
			BlocksHelper.setWithoutUpdate(world, pos.below(),
					EndBlocks.CAVE_PUMPKIN.defaultBlockState().setValue(BlockProperties.SMALL, age < 3));
		}

		return true;
	}
}
