package ru.betterend.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseRotatedPillarBlock;
import ru.bclib.util.BlocksHelper;

public class AmaranitaStemBlock extends BaseRotatedPillarBlock {
	public AmaranitaStemBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.OAK_PLANKS).materialColor(MaterialColor.COLOR_LIGHT_GREEN));
	}
}
