package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.bclib.blocks.FeatureSaplingBlock;
import ru.betterend.blocks.basis.PottableFeatureSapling;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class LucerniaSaplingBlock extends PottableFeatureSapling {
	public LucerniaSaplingBlock() {
		super();
	}
	
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.LUCERNIA.getFeature();
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.RUTISCUS);
	}
	
	@Override
	public boolean canPlantOn(Block block) {
		return false;
	}
}
