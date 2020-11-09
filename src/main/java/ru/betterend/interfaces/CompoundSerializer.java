package ru.betterend.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface CompoundSerializer<T> {
	public abstract CompoundTag beToTag(CompoundTag tag);
	public abstract T beFromTag(CompoundTag tag);
}
