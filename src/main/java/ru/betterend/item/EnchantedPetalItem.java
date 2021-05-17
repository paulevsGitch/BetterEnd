package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndItems;

public class EnchantedPetalItem extends ModelProviderItem {
	public EnchantedPetalItem() {
		super(EndItems.makeItemSettings().rarity(Rarity.RARE).stacksTo(16));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
	@Override
	public String getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, "item/hydralux_petal");
	}

	@Override
	public BlockModel getModel(ResourceLocation resourceLocation) {
		return ModelProvider.createItemModel("hydralux_petal");
	}
}
