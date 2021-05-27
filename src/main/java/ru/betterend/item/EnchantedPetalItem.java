package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.registry.EndItems;

public class EnchantedPetalItem extends ModelProviderItem {
	public EnchantedPetalItem() {
		super(EndItems.makeEndItemSettings().rarity(Rarity.RARE).stacksTo(16));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createItemModel("hydralux_petal");
	}
}
