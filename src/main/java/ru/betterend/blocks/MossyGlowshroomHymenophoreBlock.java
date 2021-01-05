package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BaseBlock;

public class MossyGlowshroomHymenophoreBlock extends BaseBlock {
	public MossyGlowshroomHymenophoreBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sounds(BlockSoundGroup.WART_BLOCK).luminance(15));
	}
}
