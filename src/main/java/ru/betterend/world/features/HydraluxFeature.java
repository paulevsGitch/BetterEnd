package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.HydraluxSaplingBlock;
import ru.betterend.registry.EndBlocks;

public class HydraluxFeature extends UnderwaterPlantScatter {
	public HydraluxFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		HydraluxSaplingBlock seed = (HydraluxSaplingBlock) EndBlocks.HYDRALUX_SAPLING;
		seed.grow(world, random, blockPos);
	}

	@Override
	protected int getChance() {
		return 15;
	}
}
