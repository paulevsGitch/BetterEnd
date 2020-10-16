package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockEndLilySeed;
import ru.betterend.registry.BlockRegistry;

public class EndLilyFeature extends UnderwaterPlantScatter {
	public EndLilyFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockEndLilySeed seed = (BlockEndLilySeed) BlockRegistry.END_LILY_SEED;
		seed.grow(world, random, blockPos);
	}
	
	@Override
	protected int getChance() {
		return 15;
	}
}
