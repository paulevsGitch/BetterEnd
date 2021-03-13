package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import ru.betterend.blocks.basis.BlockBase;

public class FilaluxLanternBlock extends BlockBase {
	public FilaluxLanternBlock() {
		super(FabricBlockSettings.of(Material.WOOD).luminance(15));
	}
}
