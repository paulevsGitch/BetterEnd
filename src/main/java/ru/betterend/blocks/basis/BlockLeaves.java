package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MaterialColor;

public class BlockLeaves extends LeavesBlock {
	public BlockLeaves(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).materialColor(color));
	}
	
	public BlockLeaves(MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).materialColor(color).lightLevel(light));
	}
}
