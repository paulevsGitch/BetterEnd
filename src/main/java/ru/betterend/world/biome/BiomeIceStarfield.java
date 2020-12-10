package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;

public class BiomeIceStarfield extends EndBiome {
	public BiomeIceStarfield() {
		super(new BiomeDefinition("ice_starfield")
				.setFogColor(224, 245, 254)
				.setFogDensity(2.2F)
				.setFoliageColor(193, 244, 244)
				.setParticles(EndParticles.SNOWFLAKE, 0.001F)
				.addFeature(EndFeatures.ICE_STAR)
				.addMobSpawn(EntityType.ENDERMAN, 20, 1, 4));
	}
}
