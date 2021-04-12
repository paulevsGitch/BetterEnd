package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import ru.betterend.blocks.EndLotusSeedBlock;
import ru.betterend.registry.EndBlocks;

public class EndLotusFeature extends UnderwaterPlantScatter {
	public EndLotusFeature(int radius) {
		super(radius);
	}

	@Override
	public void place(WorldGenLevel world, Random random, BlockPos blockPos) {
		EndLotusSeedBlock seed = (EndLotusSeedBlock) EndBlocks.END_LOTUS_SEED;
		seed.grow(world, random, blockPos);
	}

	@Override
	protected int getChance() {
		return 15;
	}
}
