package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ru.betterend.client.models.ItemModelProvider;
import ru.betterend.client.models.ModelsHelper;

public class ModelProviderItem extends Item implements ItemModelProvider {
	public ModelProviderItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createItemModel(resourceLocation.getPath());
	}
}
