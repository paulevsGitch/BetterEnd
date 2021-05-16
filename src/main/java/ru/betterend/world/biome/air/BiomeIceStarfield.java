package ru.betterend.world.biome.air;

import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class BiomeIceStarfield extends EndBiome {
	public BiomeIceStarfield() {
		super(new BiomeDefinition("ice_starfield")
				.setFogColor(224, 245, 254)
				.setTemperature(0F)
				.setFogDensity(2.2F)
				.setFoliageColor(193, 244, 244)
				.setGenChance(0.25F)
				.setCaves(false)
				.setParticles(EndParticles.SNOWFLAKE, 0.002F)
				.addStructureFeature(EndStructures.GIANT_ICE_STAR)
				.addFeature(EndFeatures.ICE_STAR)
				.addFeature(EndFeatures.ICE_STAR_SMALL)
				.addMobSpawn(EntityType.ENDERMAN, 20, 1, 4));
	}
}
