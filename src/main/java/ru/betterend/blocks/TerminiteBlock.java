package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.SoundType;
import ru.betterend.blocks.basis.BlockBase;

public class TerminiteBlock extends BlockBase {
	public TerminiteBlock() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.field_25708).hardness(7F).resistance(9F)
				.requiresTool().sounds(SoundType.METAL));
	}
}
