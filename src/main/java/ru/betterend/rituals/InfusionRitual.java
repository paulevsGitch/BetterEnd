package ru.betterend.rituals;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.recipe.builders.InfusionRecipe;

public class InfusionRitual implements Inventory {

	private final World world;
	private final BlockPos worldPos;
	private InfusionRecipe activeRecipe;
	private int progress = 0;
	private int time = 0;
	
	public InfusionRitual(World world, BlockPos pos) {
		this.world = world;
		this.worldPos = pos;
	}
	
	public void tick() {
		if (!hasRecipe()) return;
		this.progress++;
		if (progress == time) {
			
		}
	}
	
	public boolean hasRecipe() {
		return this.activeRecipe != null;
	}

	@Override
	public void clear() {
		// TODO
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
		return true;
	}
	
	public void fromTag(CompoundTag tag) {
	}

	public CompoundTag toTag(CompoundTag tag) {
		return tag;
	}
}
