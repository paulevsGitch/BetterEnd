package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import ru.betterend.blocks.basis.EndPillarBlock;

public class AmaranitaStemBlock extends EndPillarBlock {
	public AmaranitaStemBlock() {
		super(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).materialColor(MaterialColor.COLOR_LIGHT_GREEN));
	}
}
