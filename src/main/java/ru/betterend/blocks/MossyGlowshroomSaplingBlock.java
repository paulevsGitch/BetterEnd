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

public class MossyGlowshroomSaplingBlock extends PottableFeatureSapling {
	public MossyGlowshroomSaplingBlock() {
		super(7);
	}
	
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.MOSSY_GLOWSHROOM.getFeature();
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.END_MOSS) || world.getBlockState(pos.below()).is(EndBlocks.END_MYCELIUM);
	}
	
	@Override
	public boolean canPlantOn(Block block) {
		return block == EndBlocks.END_MOSS || block == EndBlocks.END_MYCELIUM;
	}
}
