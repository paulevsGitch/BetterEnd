package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockHydraluxSapling;
import ru.betterend.registry.EndBlocks;

public class HydraluxFeature extends UnderwaterPlantScatter {
	public HydraluxFeature(int radius) {
		super(radius);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockHydraluxSapling seed = (BlockHydraluxSapling) EndBlocks.HYDRALUX_SAPLING;
		seed.grow(world, random, blockPos);
	}
	
	@Override
	protected int getChance() {
		return 15;
	}
}
