package ru.betterend.patterns;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelProvider {
	String getModelString(String name);

	default BlockModel getModel(ResourceLocation resourceLocation) {
		return createItemModel(resourceLocation.getPath());
	}

	static BlockModel createItemModel(String name) {
		String pattern = Patterns.createItemGenerated("item/" + name);
		return BlockModel.fromString(pattern);
	}
}