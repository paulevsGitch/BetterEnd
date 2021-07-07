package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.blocks.NeonCactusPlantBlock;
import ru.betterend.registry.EndBlocks;

public class NeonCactusFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		BlockState ground = world.getBlockState(pos.below());
		if (!ground.is(EndBlocks.ENDSTONE_DUST) && !ground.is(EndBlocks.END_MOSS)) {
			return false;
		}
		
		NeonCactusPlantBlock cactus = ((NeonCactusPlantBlock) EndBlocks.NEON_CACTUS);
		cactus.growPlant(world, pos, random);

		return true;
	}
}
