package ru.betterend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import ru.betterend.blocks.basis.BlockPlant;

public class BlockTerrainPlant extends BlockPlant {
	private final Block ground;
	
	public BlockTerrainPlant(Block ground) {
		super(true);
		this.ground = ground;
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(ground);
	}
}
