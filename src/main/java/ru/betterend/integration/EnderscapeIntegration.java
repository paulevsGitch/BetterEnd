package ru.betterend.integration;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import ru.bclib.api.BiomeAPI;
import ru.bclib.integration.ModIntegration;

public class EnderscapeIntegration extends ModIntegration {
	public EnderscapeIntegration() {
		super("enderscape");
	}
	
	@Override
	public void init() {
		Class<?> enderscape = getClass("net.enderscape.Enderscape");
		Class<?> enderscapeIslandsBiome = getClass("net.enderscape.world.biomes.EnderscapeIslandsBiome");
		MappedRegistry<?> biomes = getStaticFieldValue(enderscape, "ENDERSCAPE_BIOME");
		biomes.entrySet().forEach(entry -> {
			ResourceKey key = entry.getKey();
			Biome biome = getBiome(key.location().getPath());
			if (enderscapeIslandsBiome.isInstance(entry.getValue())) {
				BiomeAPI.registerEndVoidBiome(biome);
			}
			else {
				BiomeAPI.registerEndLandBiome(biome);
			}
		});
	}
}
