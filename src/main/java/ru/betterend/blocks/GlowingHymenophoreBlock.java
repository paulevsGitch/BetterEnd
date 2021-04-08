package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.SoundType;
import ru.betterend.blocks.basis.BlockBase;

public class GlowingHymenophoreBlock extends BlockBase {
	public GlowingHymenophoreBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sounds(SoundType.WART_BLOCK)
				.luminance(15));
	}
}
