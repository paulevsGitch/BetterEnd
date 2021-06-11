package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;

public class TerminiteBlock extends BaseBlock {
	public TerminiteBlock() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.WARPED_WART_BLOCK)
				.hardness(7F)
				.resistance(9F)
				.requiresCorrectToolForDrops()
				.sound(SoundType.METAL));
	}
}
