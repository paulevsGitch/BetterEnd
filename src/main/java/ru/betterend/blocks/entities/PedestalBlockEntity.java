package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockPedestal;
import ru.betterend.registry.EndBlockEntities;

public class PedestalBlockEntity extends BlockEntity implements Inventory, Tickable {
	private ItemStack activeItem = ItemStack.EMPTY;
	
	private final int maxAge = 314;
	private int age;
	
	public PedestalBlockEntity() {
		super(EndBlockEntities.PEDESTAL);
	}
	
	public int getAge() {
		return this.age;
	}
	
	public int getMaxAge() {
		return this.maxAge;
	}

	@Override
	public void clear() {
		this.activeItem = ItemStack.EMPTY;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.activeItem.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.activeItem;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return this.removeStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return this.activeItem = ItemStack.EMPTY;
	}
	
	public void removeStack(World world, BlockState state) {
		world.setBlockState(pos, state.with(BlockPedestal.HAS_ITEM, false));
		this.removeStack(0);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.activeItem = stack;
	}
	
	public void setStack(World world, BlockState state, ItemStack stack) {
		world.setBlockState(pos, state.with(BlockPedestal.HAS_ITEM, true));
		this.setStack(0, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 32, this.toInitialChunkDataTag());
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return this.toTag(new CompoundTag());
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		if (tag.contains("active_item")) {
			CompoundTag itemTag = tag.getCompound("active_item");
			this.activeItem = ItemStack.fromTag(itemTag);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.put("active_item", activeItem.toTag(new CompoundTag()));
		return tag;
	}

	@Override
	public void tick() {
		if (!isEmpty()) {
			this.age++;
			if (age > maxAge) {
				this.age = 0;
			}
		}
	}
}
