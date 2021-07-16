package ru.betterend.blocks;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends EndAnvilBlock {
	
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.defaultMaterialColor(), EndToolMaterial.AETERNIUM.getLevel());
	}
	
	@Override
	public IntegerProperty getDurability() {
		if (durability == null) {
			this.maxDurability = 8;
			this.durability = IntegerProperty.create("durability", 0, maxDurability);
		}
		return durability;
	}
}
