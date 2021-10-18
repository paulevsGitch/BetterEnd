package ru.betterend.blocks.basis;

import ru.bclib.api.TagAPI;
import ru.bclib.blocks.FeatureSaplingBlock;
import ru.betterend.interfaces.PottablePlant;

public abstract class PottableFeatureSapling extends FeatureSaplingBlock implements PottablePlant {
	public PottableFeatureSapling() {
		super();
		TagAPI.addTags(this, TagAPI.BLOCK_SAPLINGS);
	}
	
	public PottableFeatureSapling(int light) {
		super(light);
	}
}
