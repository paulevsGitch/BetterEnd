package ru.betterend.rituals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class InfusionRitual implements Inventory {

	public void tick() {
		// TODO
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return null;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return null;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return null;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		// TODO
	}

	@Override
	public void markDirty() {
		// TODO
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}
}
