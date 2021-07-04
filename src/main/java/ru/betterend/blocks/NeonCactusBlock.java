package ru.betterend.blocks;

import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseRotatedPillarBlock;
import ru.bclib.util.BlocksHelper;

public class NeonCactusBlock extends BaseRotatedPillarBlock {
	public NeonCactusBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.CACTUS).luminance(15));
	}
}
