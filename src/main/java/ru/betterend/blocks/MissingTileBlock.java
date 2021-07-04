package ru.betterend.blocks;

import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.util.BlocksHelper;

public class MissingTileBlock extends BaseBlock {
	public MissingTileBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.END_STONE));
	}
}
