package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.bclib.blocks.FeatureSaplingBlock;
import ru.bclib.client.render.BCLRenderLayer;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class UmbrellaTreeSaplingBlock extends FeatureSaplingBlock {
	public UmbrellaTreeSaplingBlock() {
		super();
	}
	
	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.UMBRELLA_TREE.getFeature();
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.JUNGLE_MOSS);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}
}
