package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import ru.betterend.blocks.basis.LitPillarBlock;

public class SmaragdantCrystalBlock extends LitPillarBlock {
	public SmaragdantCrystalBlock() {
		super(FabricBlockSettings.of(Material.GLASS)
								 .breakByTool(FabricToolTags.PICKAXES)
								 .luminance(15)
								 .hardness(1F)
								 .resistance(1F)
								 .noOcclusion()
								 .sound(SoundType.AMETHYST));
	}
}
