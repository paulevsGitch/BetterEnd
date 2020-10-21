package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.util.BlocksHelper;

public class CavePlantFeature extends FullHeightScatterFeature {
	private final Block plant;
	
	public CavePlantFeature(Block plant, int radius) {
		super(radius);
		this.plant = plant;
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return plant.canPlaceAt(world.getBlockState(blockPos), world, blockPos);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlocksHelper.setWithoutUpdate(world, blockPos, plant);
	}
}
