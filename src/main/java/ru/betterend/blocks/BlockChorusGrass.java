package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import ru.betterend.blocks.basis.BlockPlant;
import ru.betterend.registry.EndBlocks;

public class BlockChorusGrass extends BlockPlant {
	public BlockChorusGrass() {
		super(true);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.CHORUS_NYLIUM;
	}
}
