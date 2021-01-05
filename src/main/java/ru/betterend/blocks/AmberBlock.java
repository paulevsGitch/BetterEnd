package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import ru.betterend.blocks.basis.BaseBlock;

public class AmberBlock extends BaseBlock {
	public AmberBlock() {
		super(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).materialColor(MaterialColor.YELLOW));
	}
}
