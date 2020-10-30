package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import ru.betterend.registry.EndBlockEntities;
import ru.betterend.util.EternalRitual;

public class PedestalBlockEntity extends BlockEntity implements Inventory, Tickable {
	private ItemStack activeItem = ItemStack.EMPTY;
	
	private EternalRitual linkedRitual;
	private int age;
	
	public PedestalBlockEntity() {
		super(EndBlockEntities.PEDESTAL);
	}
	
	public boolean hasRitual() {
		return this.linkedRitual != null;
	}
	
	public void linkRitual(EternalRitual ritual) {
		this.linkedRitual = ritual;
	}
	
	public EternalRitual getRitual() {
		return this.linkedRitual;
	}
	
	public int getAge() {
		return this.age;
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

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.activeItem = stack;
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
	public void setLocation(World world, BlockPos pos) {
		super.setLocation(world, pos);
		if (hasRitual()) {
			this.linkedRitual.setWorld(world);
		}
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		if (tag.contains("active_item")) {
			CompoundTag itemTag = tag.getCompound("active_item");
			this.activeItem = ItemStack.fromTag(itemTag);
		}
		if (tag.contains("ritual")) {
			this.linkedRitual = new EternalRitual(world);
			this.linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("active_item", activeItem.toTag(new CompoundTag()));
		if (this.hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return super.toTag(tag);
	}

	@Override
	public void tick() {
		if (!isEmpty()) {
			this.age++;
			if (age > 314) {
				this.age = 0;
			}
		}
	}
}
