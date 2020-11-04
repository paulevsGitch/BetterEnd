package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

public class WallPlantOnLogFeature extends WallPlantFeature {
	public WallPlantOnLogFeature(Block block, int radius) {
		super(block, radius);
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		BlockPos blockPos = pos.offset(dir.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isIn(BlockTags.LOGS);
	}
}
