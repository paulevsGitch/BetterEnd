package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseTerrainBlock;

public class EndTerrainBlock extends BaseTerrainBlock {
	public EndTerrainBlock(MaterialColor color) {
		super(Blocks.END_STONE, color);
	}
}
