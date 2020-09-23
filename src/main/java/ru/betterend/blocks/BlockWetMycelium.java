package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockWetMycelium extends Block {
	public BlockWetMycelium() {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE));
	}
}
