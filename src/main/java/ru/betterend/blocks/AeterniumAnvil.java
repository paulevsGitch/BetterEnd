package ru.betterend.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import ru.bclib.items.BaseAnvilItem;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends EndAnvilBlock {
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.defaultMaterialColor(), EndToolMaterial.AETERNIUM.getLevel());
	}
	
	@Override
	public int getMaxDurability() {
		return 8;
	}
	
	@Override
	public BlockItem getCustomItem(ResourceLocation blockID, FabricItemSettings settings) {
		return new BaseAnvilItem(this, settings.fireproof());
	}
}
