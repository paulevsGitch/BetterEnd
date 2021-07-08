package ru.betterend.client.gui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;

public class SmelterFuelSlot extends Slot {

	private final EndStoneSmelterScreenHandler handler;

	public SmelterFuelSlot(EndStoneSmelterScreenHandler handler, Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.handler = handler;
	}

	public boolean mayPlace(ItemStack stack) {
		return this.handler.isFuel(stack) || FurnaceFuelSlot.isBucket(stack);
	}

	public int getMaxStackSize(ItemStack stack) {
		return FurnaceFuelSlot.isBucket(stack) ? 1 : super.getMaxStackSize(stack);
	}
}
