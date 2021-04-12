package ru.betterend.world.features;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import ru.betterend.util.BlocksHelper;

public class CavePlantFeature extends FullHeightScatterFeature {
	private final Block plant;
	
	public CavePlantFeature(Block plant, int radius) {
		super(radius);
		this.plant = plant;
	}

	@Override
	public boolean canGenerate(WorldGenLevel world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return plant.canSurvive(world.getBlockState(blockPos), world, blockPos);
	}

	@Override
	public void generate(WorldGenLevel world, Random random, BlockPos blockPos) {
		BlocksHelper.setWithoutUpdate(world, blockPos, plant);
	}
}
