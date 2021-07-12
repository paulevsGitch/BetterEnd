package ru.betterend.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.item.EndAnvilItem;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class AeterniumAnvil extends EndAnvilBlock {
	
	protected final Item anvilItem;
	
	public AeterniumAnvil() {
		super(EndBlocks.AETERNIUM_BLOCK.defaultMaterialColor(), EndToolMaterial.AETERNIUM.getLevel());
		this.anvilItem = EndItems.registerEndItem("aeternuim_anvil_item", new EndAnvilItem(this));
	}
	
	@Override
	public IntegerProperty getDurability() {
		if (durability == null) {
			this.maxDurability = 8;
			this.durability = IntegerProperty.create("durability", 0, maxDurability);
		}
		return durability;
	}
	
	@Override
	public Item asItem() {
		return anvilItem;
	}
}
