package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.FeatureSaplingBlock;
import ru.betterend.client.render.ERenderLayer;
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
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isOf(EndBlocks.JUNGLE_MOSS);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
}
