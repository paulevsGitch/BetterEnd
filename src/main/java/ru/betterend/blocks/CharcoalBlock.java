package ru.betterend.blocks;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.util.BlocksHelper;

public class CharcoalBlock extends BaseBlock {
	public CharcoalBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.COAL_BLOCK));
		FuelRegistry.INSTANCE.add(this, 16000);
	}
}
