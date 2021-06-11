package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseRotatedPillarBlock;

public class NeonCactusBlock extends BaseRotatedPillarBlock {
	public NeonCactusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.CACTUS).luminance(15));
	}
}
