package ru.betterend.patterns;

import net.minecraft.client.renderer.block.model.BlockModel;
import org.jetbrains.annotations.NotNull;

public interface Patterned {
	String getModelString(String name);
	@NotNull BlockModel getItemModel();
}