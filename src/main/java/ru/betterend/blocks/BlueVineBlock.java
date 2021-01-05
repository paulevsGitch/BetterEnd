package ru.betterend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UpDownPlantBlock;
import ru.betterend.registry.EndBlocks;

public class BlueVineBlock extends UpDownPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.END_MOSS || state.getBlock() == EndBlocks.END_MYCELIUM;
	}
}
