package ru.betterend.world.biome.air;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.land.BiomeDefinition;
import ru.betterend.world.biome.land.EndBiome;

public class BiomeIceStarfield extends EndBiome {
	public BiomeIceStarfield() {
		super(new BiomeDefinition("ice_starfield")
				.setFogColor(224, 245, 254)
				.setFogDensity(2.2F)
				.setFoliageColor(193, 244, 244)
				.setGenChance(0.25F)
				.setParticles(EndParticles.SNOWFLAKE, 0.002F)
				.addStructureFeature(EndStructures.GIANT_ICE_STAR)
				.addFeature(EndFeatures.ICE_STAR)
				.addFeature(EndFeatures.ICE_STAR_SMALL)
				.addMobSpawn(EntityType.ENDERMAN, 20, 1, 4));
	}
}
