package ru.betterend.blocks.entities;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import ru.betterend.registry.EndBlockEntities;

public class EChestBlockEntity extends ChestBlockEntity {
	public EChestBlockEntity() {
		super(EndBlockEntities.CHEST);
	}
}
