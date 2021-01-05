package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BaseBlock;

public class EndStoneBlock extends BaseBlock {
	public EndStoneBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color).sounds(BlockSoundGroup.STONE));
	}
}
