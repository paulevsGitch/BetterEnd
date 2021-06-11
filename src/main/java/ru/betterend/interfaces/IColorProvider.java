package ru.betterend.interfaces;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

public interface IColorProvider {
	BlockColor getProvider();
	ItemColor getItemProvider();
}
