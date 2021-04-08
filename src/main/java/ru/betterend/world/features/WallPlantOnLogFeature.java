package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.StructureWorldAccess;

public class WallPlantOnLogFeature extends WallPlantFeature {
	public WallPlantOnLogFeature(Block block, int radius) {
		super(block, radius);
	}

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos pos, Direction dir) {
		BlockPos blockPos = pos.relative(dir.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isIn(BlockTags.LOGS);
	}
}
