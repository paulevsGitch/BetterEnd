package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class SmaragdantCrystalFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).isIn(EndTags.GEN_TERRAIN)) {
			return false;
		}

		MutableBlockPos mut = new MutableBlockPos();
		int count = MHelper.randRange(15, 30, random);
		BlockState crystal = EndBlocks.SMARAGDANT_CRYSTAL.defaultBlockState();
		BlockState shard = EndBlocks.SMARAGDANT_CRYSTAL_SHARD.defaultBlockState();
		for (int i = 0; i < count; i++) {
			mut.set(pos).move(MHelper.floor(random.nextGaussian() * 2 + 0.5), 5,
					MHelper.floor(random.nextGaussian() * 2 + 0.5));
			int dist = MHelper.floor(1.5F - MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ()))
					+ random.nextInt(3);
			if (dist > 0) {
				BlockState state = world.getBlockState(mut);
				for (int n = 0; n < 10 && state.isAir(); n++) {
					mut.setY(mut.getY() - 1);
					state = world.getBlockState(mut);
				}
				if (state.isIn(EndTags.GEN_TERRAIN) && !world.getBlockState(mut.up()).is(crystal.getBlock())) {
					for (int j = 0; j <= dist; j++) {
						BlocksHelper.setWithoutUpdate(world, mut, crystal);
						mut.setY(mut.getY() + 1);
					}
					boolean waterlogged = !world.getFluidState(mut).isEmpty();
					BlocksHelper.setWithoutUpdate(world, mut, shard.with(Properties.WATERLOGGED, waterlogged));
				}
			}
		}

		return true;
	}
}
