package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public class TerminiteBlock extends BlockBase {

	public TerminiteBlock() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.field_25708)
				.hardness(7F)
				.resistance(9F)
				.requiresTool()
				.sounds(BlockSoundGroup.METAL));
	}
}
