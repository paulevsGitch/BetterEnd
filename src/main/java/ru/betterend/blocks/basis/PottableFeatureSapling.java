package ru.betterend.blocks.basis;

import ru.bclib.blocks.FeatureSaplingBlock;
import ru.betterend.interfaces.PottablePlant;

public abstract class PottableFeatureSapling extends FeatureSaplingBlock implements PottablePlant {
	public PottableFeatureSapling() {
		super();
	}
	
	public PottableFeatureSapling(int light) {
		super(light);
	}
}
