package ru.betterend.blocks.basis;

import net.minecraft.block.Block;

public class EndWoodenPlateBlock extends EndPlateBlock {
	public EndWoodenPlateBlock(Block source) {
		super(ActivationRule.EVERYTHING, source);
	}
}
