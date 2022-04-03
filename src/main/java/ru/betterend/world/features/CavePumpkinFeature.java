package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.api.tag.CommonBlockTags;
import ru.bclib.util.BlocksHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.blocks.EndBlockProperties;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class CavePumpkinFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (!world.getBlockState(pos.above()).is(CommonBlockTags.GEN_END_STONES) || !world.isEmptyBlock(pos) || !world.isEmptyBlock(
			pos.below())) {
			return false;
		}
		
		int age = random.nextInt(4);
		BlocksHelper.setWithoutUpdate(
			world,
			pos,
			EndBlocks.CAVE_PUMPKIN_SEED.defaultBlockState().setValue(EndBlockProperties.AGE, age)
		);
		if (age > 1) {
			BlocksHelper.setWithoutUpdate(
				world,
				pos.below(),
				EndBlocks.CAVE_PUMPKIN.defaultBlockState().setValue(EndBlockProperties.SMALL, age < 3)
			);
		}
		
		return true;
	}
}
