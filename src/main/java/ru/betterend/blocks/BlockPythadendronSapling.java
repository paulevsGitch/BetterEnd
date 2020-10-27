package ru.betterend.blocks;

import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.EndFeatures;

public class BlockPythadendronSapling extends BlockFeatureSapling {
	public BlockPythadendronSapling() {
		super();
	}

	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.PYTHADENDRON_TREE.getFeature();
	}
}
