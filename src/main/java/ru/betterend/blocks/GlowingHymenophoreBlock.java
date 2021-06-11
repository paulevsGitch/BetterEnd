package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import ru.bclib.blocks.BaseBlock;

public class GlowingHymenophoreBlock extends BaseBlock {
	public GlowingHymenophoreBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).luminance(15).sound(SoundType.WART_BLOCK));
	}
}
