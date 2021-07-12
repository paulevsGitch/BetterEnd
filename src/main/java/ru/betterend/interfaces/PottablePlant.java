package ru.betterend.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;

public interface PottablePlant {
	boolean canPlantOn(Block block);
	
	default boolean addToPot() {
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	default String getPottedState() {
		return "";
	}
}
