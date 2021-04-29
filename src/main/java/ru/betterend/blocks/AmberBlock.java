package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.betterend.blocks.basis.BlockBase;

public class AmberBlock extends BlockBase {
	public AmberBlock() {
		super(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).materialColor(MaterialColor.COLOR_YELLOW));
	}
}
