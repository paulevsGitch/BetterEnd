package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.items.ModelProviderItem;
import ru.betterend.registry.EndItems;

public class EnchantedItem extends ModelProviderItem {

	private final Item source;

	public EnchantedItem(Item source) {
		super(EndItems.makeEndItemSettings().rarity(Rarity.RARE).stacksTo(16));
		this.source = source;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		ResourceLocation sourceId = Registry.ITEM.getKey(source);
		return ModelsHelper.createItemModel(sourceId);
	}
}
