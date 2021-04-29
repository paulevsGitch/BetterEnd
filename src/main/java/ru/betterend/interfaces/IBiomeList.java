package ru.betterend.interfaces;

import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface IBiomeList {
	public List<ResourceKey<Biome>> getBiomes();
}
