package ru.betterend.blocks;

import net.minecraft.world.gen.feature.Feature;
import ru.betterend.blocks.basis.BlockFeatureSapling;
import ru.betterend.registry.EndFeatures;

public class BlockLacugroveSapling extends BlockFeatureSapling {
	public BlockLacugroveSapling() {
		super();
	}

	@Override
	protected Feature<?> getFeature() {
		return EndFeatures.LACUGROVE.getFeature();
	}
}
