package ru.betterend.patterns;

import net.minecraft.client.renderer.block.model.BlockModel;

public interface ModelProvider {
	String getModelString(String name);
	BlockModel getModel();
}