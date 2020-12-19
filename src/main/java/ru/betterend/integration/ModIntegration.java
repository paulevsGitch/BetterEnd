package ru.betterend.integration;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class ModIntegration {
	private final String modID;
	
	public ModIntegration(String modID) {
		this.modID = modID;
	}
	
	public Identifier getID(String name) {
		return new Identifier(modID, name);
	}
	
	public Block getBlock(String name) {
		return Registry.BLOCK.get(getID(name));
	}

	public BlockState getDefaultState(String name) {
		return getBlock(name).getDefaultState();
	}
	
	public RegistryKey<Biome> getKey(String name) {
		return RegistryKey.of(Registry.BIOME_KEY, getID(name));
	}
	
	public boolean modIsInstalled() {
		return FabricLoader.getInstance().isModLoaded(modID);
	}

	public abstract void register();
}
