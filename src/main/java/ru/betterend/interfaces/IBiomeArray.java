package ru.betterend.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

public interface IBiomeArray {
	public void setBiome(Biome biome, BlockPos pos);
}
