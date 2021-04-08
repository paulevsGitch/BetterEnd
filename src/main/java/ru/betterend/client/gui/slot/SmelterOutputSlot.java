package ru.betterend.client.gui.slot;

import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import ru.betterend.blocks.entities.EndStoneSmelterBlockEntity;

public class SmelterOutputSlot extends Slot {

	private PlayerEntity player;
	private int amount;

	public SmelterOutputSlot(PlayerEntity player, Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.player = player;
	}

	public boolean canInsert(ItemStack stack) {
		return false;
	}

	public ItemStack takeStack(int amount) {
		if (this.hasStack()) {
			this.amount += Math.min(amount, this.getStack().getCount());
		}

		return super.takeStack(amount);
	}

	public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
		this.onCrafted(stack);
		super.onTakeItem(player, stack);
		return stack;
	}

	protected void onCrafted(ItemStack stack, int amount) {
		this.amount += amount;
		this.onCrafted(stack);
	}

	protected void onCrafted(ItemStack stack) {
		stack.onCraft(this.player.world, this.player, this.amount);
		if (!this.player.world.isClientSide && this.inventory instanceof EndStoneSmelterBlockEntity) {
			((EndStoneSmelterBlockEntity) this.inventory).dropExperience(player);
		}
		this.amount = 0;
	}
}
