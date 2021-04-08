package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.SoundType;
import ru.betterend.blocks.basis.BlockBase;

public class EndStoneBlock extends BlockBase {
	public EndStoneBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color).sounds(SoundType.STONE));
	}
}
