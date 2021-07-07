package ru.betterend.world.features;

import net.minecraft.world.level.block.Block;

public class CharniaFeature extends UnderwaterPlantFeature {
	public CharniaFeature(Block plant) {
		super(plant, 6);
	}

	@Override
	protected int getChance() {
		return 3;
	}
}
