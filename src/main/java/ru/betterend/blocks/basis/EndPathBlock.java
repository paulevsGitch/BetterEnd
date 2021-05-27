package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BasePathBlock;

public class EndPathBlock extends BasePathBlock {

	public EndPathBlock(Block source) {
		super(source);
	}

	@Override
	protected Block getBottomBlock() {
		return Blocks.END_STONE;
	}
}
