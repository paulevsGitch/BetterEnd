package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;

public class AmberBlock extends BaseBlock {
	public AmberBlock() {
		super(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).mapColor(MaterialColor.COLOR_YELLOW));
	}
}
