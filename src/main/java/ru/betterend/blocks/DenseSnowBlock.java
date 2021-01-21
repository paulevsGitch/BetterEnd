package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BlockBase;

public class DenseSnowBlock extends BlockBase {
	public DenseSnowBlock() {
		super(FabricBlockSettings.of(Material.SNOW_BLOCK).strength(0.2F).sounds(BlockSoundGroup.SNOW));
	}
}
