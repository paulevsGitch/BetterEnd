package ru.betterend.patterns;

import net.minecraft.client.renderer.block.model.BlockModel;

public interface Patterned {
	String getModelString(String name);
	BlockModel getItemModel();
}