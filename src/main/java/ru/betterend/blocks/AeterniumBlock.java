package ru.betterend.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.interfaces.CustomItemProvider;

public class AeterniumBlock extends BaseBlock implements CustomItemProvider {
	public AeterniumBlock() {
		super(FabricBlockSettings
			.of(Material.METAL, MaterialColor.COLOR_GRAY)
			.hardness(65F)
			.resistance(1200F)
			.requiresCorrectToolForDrops()
			.sound(SoundType.NETHERITE_BLOCK)
		);
	}
	
	@Override
	public BlockItem getCustomItem(ResourceLocation blockID, FabricItemSettings settings) {
		return new BlockItem(this, settings.fireproof());
	}
}
