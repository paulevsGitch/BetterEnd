package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.SoundType;
import ru.betterend.blocks.basis.EndPillarBlock;

public class SmaragdantCrystalBlock extends EndPillarBlock {
	public SmaragdantCrystalBlock() {
		super(FabricBlockSettings.of(Material.GLASS).breakByTool(FabricToolTags.PICKAXES).sounds(SoundType.GLASS)
				.luminance(15).hardness(1F).resistance(1F).nonOpaque());
	}
}
