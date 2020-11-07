package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestalEntity extends PedestalBlockEntity {

	private InfusionRitual activeRitual;
	
	public boolean hasRitual() {
		return this.activeRitual != null;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(tag);
	}
}
