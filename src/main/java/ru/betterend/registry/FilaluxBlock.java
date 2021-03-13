package ru.betterend.registry;

import net.minecraft.block.AbstractBlock;
import ru.betterend.blocks.basis.VineBlock;

public class FilaluxBlock extends VineBlock {
	public FilaluxBlock() {
		super(15, true);
	}
	
	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}
}
