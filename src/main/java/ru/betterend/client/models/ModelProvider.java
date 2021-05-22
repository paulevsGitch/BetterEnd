package ru.betterend.client.models;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public interface ModelProvider {
	Optional<String> getModelString(String name);

	default BlockModel getModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createItemModel(resourceLocation.getPath());
	}
}