package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;

public class EndStonelateBlock extends EndPlateBlock {
	public EndStonelateBlock(Block source) {
		super(ActivationRule.MOBS, source);
	}
}
