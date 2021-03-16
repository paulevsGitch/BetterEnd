package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BlockBase;

public class FilaluxLanternBlock extends BlockBase {
	public FilaluxLanternBlock() {
		super(FabricBlockSettings.of(Material.WOOD).luminance(15).sounds(BlockSoundGroup.WOOD).breakByTool(FabricToolTags.AXES));
	}
}
