package ru.betterend.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;

public interface IBiomeArray {
	public void be_setBiome(Biome biome, BlockPos pos);
}
