package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import ru.bclib.blocks.BaseBlock;

public class DenseSnowBlock extends BaseBlock {
	public DenseSnowBlock() {
		super(FabricBlockSettings.of(Material.SNOW).strength(0.2F).sound(SoundType.SNOW));
	}
}
