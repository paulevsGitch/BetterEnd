package ru.betterend.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ru.betterend.blocks.basis.VineBlock;

public class FilaluxBlock extends VineBlock {
	public FilaluxBlock() {
		super(15, true);
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
}
