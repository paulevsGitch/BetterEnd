package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class SmallAmaranitaBlock extends EndPlantBlock {
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.SANGNUM;
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockPos bigPos = growBig(world, pos);
		if (bigPos != null) {
			return;
		}
		EndFeatures.LARGE_AMARANITA.getFeature().generate(world, null, random, pos, null);
	}
	
	private BlockPos growBig(ServerWorld world, BlockPos pos) {
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				BlockPos p = pos.add(x, 0, z);
				if (checkFrame(world, p)) {
					return p;
				}
			}
		}
		return null;
	}
	
	private boolean checkFrame(ServerWorld world, BlockPos pos) {
		return world.getBlockState(pos).isOf(this) ||
				world.getBlockState(pos.south()).isOf(this) ||
				world.getBlockState(pos.east()).isOf(this) ||
				world.getBlockState(pos.south().east()).isOf(this);
	}
}
