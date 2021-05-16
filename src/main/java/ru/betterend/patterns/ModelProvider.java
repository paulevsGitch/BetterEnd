package ru.betterend.patterns;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelProvider {
	String getModelString(String name);
	BlockModel getModel(ResourceLocation resourceLocation);
}