package ru.betterend.integration;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.world.features.EndFeature;

public abstract class ModIntegration {
	private final String modID;
	
	public abstract void register();
	
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
	
	public EndFeature getFeature(String featureID, String configuredFeatureID, GenerationStep.Feature featureStep) {
		Feature<?> feature = Registry.FEATURE.get(getID(featureID));
		ConfiguredFeature<?, ?> featureConfigured = BuiltinRegistries.CONFIGURED_FEATURE.get(getID(configuredFeatureID));
		System.out.println(feature + " " + featureConfigured);
		return new EndFeature(feature, featureConfigured, featureStep);
	}
	
	public EndFeature getFeature(String name, GenerationStep.Feature featureStep) {
		return getFeature(name, name, featureStep);
	}
	
	public ConfiguredFeature<?, ?> getConfiguredFeature(String name) {
		return BuiltinRegistries.CONFIGURED_FEATURE.get(getID(name));
	}
}
