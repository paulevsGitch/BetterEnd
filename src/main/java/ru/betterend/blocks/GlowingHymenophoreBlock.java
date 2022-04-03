package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.interfaces.tools.AddMineableAxe;

public class GlowingHymenophoreBlock extends BaseBlock implements AddMineableAxe {
	public GlowingHymenophoreBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
								 .luminance(15)
								 .sound(SoundType.WART_BLOCK));
	}
}
