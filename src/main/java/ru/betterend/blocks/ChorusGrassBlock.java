package ru.betterend.blocks;

import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class ChorusGrassBlock extends EndPlantBlock {
	public ChorusGrassBlock() {
		super(true);
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.CHORUS_NYLIUM;
	}
}
