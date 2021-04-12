package ru.betterend.blocks.entities;

import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LootableContainerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import ru.betterend.blocks.basis.EndBarrelBlock;
import ru.betterend.registry.EndBlockEntities;

public class EBarrelBlockEntity extends LootableContainerBlockEntity {
	private DefaultedList<ItemStack> inventory;
	private int viewerCount;

	private EBarrelBlockEntity(BlockEntityType<?> type) {
		super(type);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
	}

	public EBarrelBlockEntity() {
		this(EndBlockEntities.BARREL);
	}

	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (!this.serializeLootTable(tag)) {
			Inventories.toTag(tag, this.inventory);
		}

		return tag;
	}

	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(tag)) {
			Inventories.fromTag(tag, this.inventory);
		}
	}

	public int size() {
		return 27;
	}

	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	protected Text getContainerName() {
		return new TranslatableText("container.barrel");
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	public void onOpen(Player player) {
		if (!player.isSpectator()) {
			if (this.viewerCount < 0) {
				this.viewerCount = 0;
			}

			++this.viewerCount;
			BlockState blockState = this.getCachedState();
			boolean bl = (Boolean) blockState.get(BarrelBlock.OPEN);
			if (!bl) {
				this.playSound(blockState, SoundEvents.BLOCK_BARREL_OPEN);
				this.setOpen(blockState, true);
			}

			this.scheduleUpdate();
		}
	}

	private void scheduleUpdate() {
		this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
	}

	public void tick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.viewerCount = ChestBlockEntity.countViewers(this.world, this, i, j, k);
		if (this.viewerCount > 0) {
			this.scheduleUpdate();
		} else {
			BlockState blockState = this.getCachedState();
			if (!(blockState.getBlock() instanceof EndBarrelBlock)) {
				this.markRemoved();
				return;
			}

			boolean bl = (Boolean) blockState.get(BarrelBlock.OPEN);
			if (bl) {
				this.playSound(blockState, SoundEvents.BLOCK_BARREL_CLOSE);
				this.setOpen(blockState, false);
			}
		}
	}

	public void onClose(Player player) {
		if (!player.isSpectator()) {
			--this.viewerCount;
		}
	}

	private void setOpen(BlockState state, boolean open) {
		this.world.setBlockAndUpdate(this.getPos(), (BlockState) state.with(BarrelBlock.OPEN, open), 3);
	}

	private void playSound(BlockState blockState, SoundEvent soundEvent) {
		Vec3i vec3i = ((Direction) blockState.get(BarrelBlock.FACING)).getVector();
		double d = (double) this.pos.getX() + 0.5D + (double) vec3i.getX() / 2.0D;
		double e = (double) this.pos.getY() + 0.5D + (double) vec3i.getY() / 2.0D;
		double f = (double) this.pos.getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
		this.world.playLocalSound((Player) null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F,
				this.world.random.nextFloat() * 0.1F + 0.9F);
	}
}