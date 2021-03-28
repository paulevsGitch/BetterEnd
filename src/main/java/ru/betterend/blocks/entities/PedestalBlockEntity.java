package ru.betterend.blocks.entities;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Tickable;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndItems;

public class PedestalBlockEntity extends BlockEntity implements Inventory, Tickable, BlockEntityClientSerializable {
	private ItemStack activeItem = ItemStack.EMPTY;
	
	private final int maxAge = 314;
	private int age;
	
	public PedestalBlockEntity() {
		super(EndBlockEntities.PEDESTAL);
	}
	
	public PedestalBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
	
	public int getAge() {
		return age;
	}
	
	public int getMaxAge() {
		return maxAge;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return activeItem.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return activeItem;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return removeStack(slot);
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return isEmpty();
	}

	@Override
	public void clear() {
		activeItem = ItemStack.EMPTY;
		markDirty();
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stored = activeItem;
		clear();
		return stored;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		activeItem = stack;
		markDirty();
	}
	
	@Override
	public void markDirty() {
		if (world != null && !world.isClient) {
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof PedestalBlock) {
				BlockState trueState = state.with(PedestalBlock.HAS_ITEM, !isEmpty());
				if (activeItem.getItem() == EndItems.ETERNAL_CRYSTAL) {
					trueState = trueState.with(PedestalBlock.HAS_LIGHT, true);
				} else {
					trueState = trueState.with(PedestalBlock.HAS_LIGHT, false);
				}
				world.setBlockState(pos, trueState);
			}
		}
		super.markDirty();
	}

	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("active_item", activeItem.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

	protected void fromTag(CompoundTag tag) {
		if (tag.contains("active_item")) {
			CompoundTag itemTag = tag.getCompound("active_item");
			activeItem = ItemStack.fromTag(itemTag);
		}
	}

	@Override
	public void tick() {
		if (!isEmpty()) {
			age++;
			if (age > maxAge) {
				age = 0;
			}
		}
	}
}
