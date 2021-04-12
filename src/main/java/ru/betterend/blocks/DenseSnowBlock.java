package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import ru.betterend.blocks.basis.BlockBase;

public class DenseSnowBlock extends BlockBase {
	public DenseSnowBlock() {
		super(FabricBlockSettings.of(Material.SNOW).strength(0.2F).sound(SoundType.SNOW));
	}
}
