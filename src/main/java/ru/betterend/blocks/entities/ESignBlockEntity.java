package ru.betterend.blocks.entities;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import ru.betterend.registry.EndBlockEntities;

public class ESignBlockEntity extends SignBlockEntity {
	public ESignBlockEntity() {
		super();
	}

	@Override
	public BlockEntityType<?> getType() {
		return EndBlockEntities.SIGN;
	}
}