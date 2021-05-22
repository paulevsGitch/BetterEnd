package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndItems;

import java.util.Optional;

public class EnchantedPetalItem extends ModelProviderItem {
	public EnchantedPetalItem() {
		super(EndItems.makeItemSettings().rarity(Rarity.RARE).stacksTo(16));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
	@Override
	public Optional<String> getModelString(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, "item/hydralux_petal");
	}

	@Override
	public BlockModel getModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createItemModel("hydralux_petal");
	}
}
