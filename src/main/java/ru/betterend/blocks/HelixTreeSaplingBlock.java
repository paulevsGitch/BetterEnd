package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.betterend.blocks.basis.PottableFeatureSapling;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class HelixTreeSaplingBlock extends PottableFeatureSapling {
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.HELIX_TREE.getFeature();
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.AMBER_MOSS);
	}
	
	@Override
	public boolean canPlantOn(Block block) {
		return block == EndBlocks.AMBER_MOSS;
	}
}
