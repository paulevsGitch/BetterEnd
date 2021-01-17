package ru.betterend.interfaces;

import net.minecraft.util.math.BlockPos;

public interface TeleportingEntity {
	void beSetExitPos(BlockPos pos);
	void beResetExitPos();
	boolean beCanTeleport();
}
