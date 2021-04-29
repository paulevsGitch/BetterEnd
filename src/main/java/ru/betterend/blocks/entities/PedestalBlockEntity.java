package ru.betterend.blocks.entities;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndItems;

public class PedestalBlockEntity extends BlockEntity implements Container, TickableBlockEntity, BlockEntityClientSerializable {
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
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return activeItem.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return activeItem;
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return removeItemNoUpdate(slot);
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return isEmpty();
	}

	@Override
	public void clearContent() {
		activeItem = ItemStack.EMPTY;
		setChanged();
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack stored = activeItem;
		clearContent();
		return stored;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		activeItem = stack;
		setChanged();
	}
	
	@Override
	public void setChanged() {
		if (level != null && !level.isClientSide) {
			BlockState state = level.getBlockState(worldPosition);
			if (state.getBlock() instanceof PedestalBlock) {
				BlockState trueState = state.setValue(PedestalBlock.HAS_ITEM, !isEmpty());
				if (activeItem.getItem() == EndItems.ETERNAL_CRYSTAL) {
					trueState = trueState.setValue(PedestalBlock.HAS_LIGHT, true);
				} else {
					trueState = trueState.setValue(PedestalBlock.HAS_LIGHT, false);
				}
				level.setBlockAndUpdate(worldPosition, trueState);
			}
		}
		super.setChanged();
	}

	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		fromTag(tag);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.put("active_item", activeItem.save(new CompoundTag()));
		return super.save(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return save(tag);
	}

	protected void fromTag(CompoundTag tag) {
		if (tag.contains("active_item")) {
			CompoundTag itemTag = tag.getCompound("active_item");
			activeItem = ItemStack.of(itemTag);
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
