package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
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
