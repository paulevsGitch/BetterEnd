package ru.betterend.interfaces;

import net.minecraft.core.BlockPos;

public interface TeleportingEntity {
	void beSetExitPos(BlockPos pos);

	void beResetExitPos();

	boolean beCanTeleport();
}
