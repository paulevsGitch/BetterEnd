package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.basis.PlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class BlueVineFeature extends ScatterFeature {
	private boolean small;
	
	public BlueVineFeature() {
		super(5);
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		float d = MHelper.length(center.getX() - blockPos.getX(), center.getZ() - blockPos.getZ()) / radius * 0.6F + random.nextFloat() * 0.4F;
		small = d > 0.5F;
		return EndBlocks.BLUE_VINE_SEED.canPlaceAt(AIR, world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		if (small) {
			BlocksHelper.setWithoutUpdate(world, blockPos, EndBlocks.BLUE_VINE_SEED.getDefaultState().with(PlantWithAgeBlock.AGE, random.nextInt(4)));
		}
		else {
			PlantWithAgeBlock seed = ((PlantWithAgeBlock) EndBlocks.BLUE_VINE_SEED);
			seed.growAdult(world, random, blockPos);
		}
	}
}
