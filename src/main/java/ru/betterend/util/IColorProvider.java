package ru.betterend.util;

import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;

public interface IColorProvider {
	BlockColorProvider getProvider();

	ItemColorProvider getItemProvider();
}
