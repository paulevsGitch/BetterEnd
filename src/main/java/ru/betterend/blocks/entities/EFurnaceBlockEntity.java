package ru.betterend.blocks.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import ru.betterend.registry.EndBlockEntities;

public class EFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
	public EFurnaceBlockEntity() {
		super(EndBlockEntities.FURNACE, RecipeType.SMELTING);
	}

	protected Component getDefaultName() {
		return new TranslatableComponent("container.furnace");
	}

	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new FurnaceMenu(syncId, playerInventory, this, this.dataAccess);
	}
}
