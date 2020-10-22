package ru.betterend.interfaces;

import net.minecraft.util.math.BlockPos;

public interface TeleportingEntity {
	public abstract long beGetCooldown();
	public abstract void beSetCooldown(long time);
	public abstract void beSetExitPos(BlockPos pos);
	public abstract BlockPos beGetExitPos();
	
	default boolean hasCooldown() {
		return this.beGetCooldown() > 0;
	}
}
