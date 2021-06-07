package ru.betterend.world.biome;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.config.Configs;

public class EndBiome extends BCLBiome {
	public EndBiome(BCLBiomeDef definition) {
		super(definition.loadConfigValues(Configs.BIOME_CONFIG));
	}

	public EndBiome(ResourceLocation id, Biome biome, float fogDensity, float genChance, boolean hasCaves) {
		super(id, biome, fogDensity, genChance);
		this.addCustomData("has_caves", hasCaves);
	}
}
