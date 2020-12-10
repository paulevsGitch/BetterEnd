package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BlockBase;

public class BlockHelixTreeLuminophor extends BlockBase {
	public BlockHelixTreeLuminophor() {
		super(FabricBlockSettings.of(Material.LEAVES)
				.materialColor(MaterialColor.ORANGE)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.strength(0.2F)
				.luminance(15));
	}
}
