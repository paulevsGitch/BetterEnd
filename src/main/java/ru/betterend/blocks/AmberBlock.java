package ru.betterend.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.util.BlocksHelper;

public class AmberBlock extends BaseBlock {
	public AmberBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.DIAMOND_BLOCK).materialColor(MaterialColor.COLOR_YELLOW));
	}
}
