package ru.betterend.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface CompoundSerializer<T> {
	public abstract CompoundTag toTag(CompoundTag tag);
	public abstract T fromTag(CompoundTag tag);
}
