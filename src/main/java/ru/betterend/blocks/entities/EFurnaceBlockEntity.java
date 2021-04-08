package ru.betterend.blocks.entities;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import ru.betterend.registry.EndBlockEntities;

public class EFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
	public EFurnaceBlockEntity() {
		super(EndBlockEntities.FURNACE, RecipeType.SMELTING);
	}

	protected Text getContainerName() {
		return new TranslatableText("container.furnace");
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
}
