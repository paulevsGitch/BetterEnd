package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseTerrainBlock;
import ru.betterend.interfaces.PottableTerrain;

public class EndTerrainBlock extends BaseTerrainBlock implements PottableTerrain {
	public EndTerrainBlock(MaterialColor color) {
		super(Blocks.END_STONE, color);
	}
}
