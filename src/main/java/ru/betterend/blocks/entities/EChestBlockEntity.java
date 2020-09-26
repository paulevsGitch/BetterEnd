package ru.betterend.blocks.entities;

import net.minecraft.block.entity.ChestBlockEntity;
import ru.betterend.registry.BlockEntityRegistry;

public class EChestBlockEntity extends ChestBlockEntity {
	public EChestBlockEntity() {
		super(BlockEntityRegistry.CHEST);
	}
}
