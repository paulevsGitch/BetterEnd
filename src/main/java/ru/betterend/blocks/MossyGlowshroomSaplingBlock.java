package ru.betterend.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.FeatureSaplingBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class MossyGlowshroomSaplingBlock extends FeatureSaplingBlock {
	public MossyGlowshroomSaplingBlock() {
		super(7);
	}

	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.MOSSY_GLOWSHROOM.getFeature();
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(EndBlocks.END_MOSS)
				|| world.getBlockState(pos.below()).is(EndBlocks.END_MYCELIUM);
	}
}
