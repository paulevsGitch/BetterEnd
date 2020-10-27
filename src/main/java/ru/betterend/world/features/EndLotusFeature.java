package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockEndLotusSeed;
import ru.betterend.registry.EndBlocks;

public class EndLotusFeature extends UnderwaterPlantScatter {
	public EndLotusFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockEndLotusSeed seed = (BlockEndLotusSeed) EndBlocks.END_LOTUS_SEED;
		seed.grow(world, random, blockPos);
	}
	
	@Override
	protected int getChance() {
		return 15;
	}
}
