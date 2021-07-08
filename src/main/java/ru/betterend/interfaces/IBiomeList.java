package ru.betterend.interfaces;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public interface IBiomeList {
	public List<ResourceKey<Biome>> getBiomes();
}
