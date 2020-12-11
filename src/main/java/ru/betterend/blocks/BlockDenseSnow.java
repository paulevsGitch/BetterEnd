package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import ru.betterend.blocks.basis.BlockBase;

public class BlockDenseSnow extends BlockBase {
	public BlockDenseSnow() {
		super(FabricBlockSettings.copyOf(Blocks.SNOW));
	}
}
