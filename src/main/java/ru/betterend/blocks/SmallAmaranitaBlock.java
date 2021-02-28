package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class SmallAmaranitaBlock extends EndPlantBlock {
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.SANGNUM;
	}
}
