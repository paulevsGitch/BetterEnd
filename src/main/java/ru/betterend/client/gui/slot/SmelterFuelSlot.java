package ru.betterend.client.gui.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.Slot;
import ru.betterend.client.gui.EndStoneSmelterScreenHandler;

public class SmelterFuelSlot extends Slot {

	private final EndStoneSmelterScreenHandler handler;
	
	public SmelterFuelSlot(EndStoneSmelterScreenHandler handler, Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.handler = handler;
	}
	
	public boolean canInsert(ItemStack stack) {
		return this.handler.isFuel(stack) || FurnaceFuelSlot.isBucket(stack);
	}

	public int getMaxItemCount(ItemStack stack) {
		return FurnaceFuelSlot.isBucket(stack) ? 1 : super.getMaxItemCount(stack);
	}
}
