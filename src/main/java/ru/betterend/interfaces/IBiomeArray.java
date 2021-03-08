package ru.betterend.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IBiomeArray {
	public void setBiome(Biome biome, BlockPos pos);
}
