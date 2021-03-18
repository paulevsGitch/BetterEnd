package ru.betterend.interfaces;

import java.util.List;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public interface IBiomeList {
	public List<RegistryKey<Biome>> getBiomes();
}
