package ru.betterend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import ru.betterend.blocks.basis.EndPlantBlock;

public class TerrainPlantBlock extends EndPlantBlock {
	private final Block ground;
	
	public TerrainPlantBlock(Block ground) {
		super(true);
		this.ground = ground;
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(ground);
	}
}
