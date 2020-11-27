package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class BlockTenaneaSapling extends BlockFeatureSapling {
	public BlockTenaneaSapling() {
		super();
	}

	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.TENANEA.getFeature();
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isOf(EndBlocks.PINK_MOSS);
	}
}
