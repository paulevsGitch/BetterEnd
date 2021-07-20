package ru.betterend.blocks;

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
}
