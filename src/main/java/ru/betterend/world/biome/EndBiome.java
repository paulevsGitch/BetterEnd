package ru.betterend.world.biome;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import ru.bclib.world.biomes.BCLBiome;
import ru.betterend.config.Configs;

public class EndBiome extends BCLBiome {
	private final boolean hasCaves;
	
	public EndBiome(BiomeDefinition definition) {
		super(definition.loadConfigValues(Configs.BIOME_CONFIG));
		this.hasCaves = Configs.BIOME_CONFIG.getBoolean(mcID, "has_caves", definition.hasCaves());
	}

	public EndBiome(ResourceLocation id, Biome biome, float fogDensity, float genChance, boolean hasCaves) {
		super(id, biome, fogDensity, genChance);
		this.hasCaves = Configs.BIOME_CONFIG.getBoolean(mcID, "has_caves", hasCaves);
	}
	
	public boolean hasCaves() {
		return hasCaves;
	}
}
