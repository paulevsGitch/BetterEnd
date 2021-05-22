package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class ModelProviderItem extends Item implements ModelProvider {
	public ModelProviderItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Optional<String> getModelString(String name) {
		return Patterns.createItemGenerated(name);
	}

	@Override
	public BlockModel getModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createItemModel(resourceLocation.getPath());
	}
}
