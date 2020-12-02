package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.world.surface.SurfaceBuilders;

public class BiomeSulfurSprings extends EndBiome {
	public BiomeSulfurSprings() {
		super(new BiomeDefinition("sulfur_springs")
				.setSurface(SurfaceBuilders.SULPHURIC_SURFACE)
				.setWaterAndFogColor(105, 213, 244)
				.setFogColor(207, 194, 62)
				.setFogDensity(1.5F)
				.setCaves(false)
				.setParticles(EndParticles.SULPHUR_PARTICLE, 0.001F)
				.addFeature(EndFeatures.GEYSER)
				.addFeature(EndFeatures.SULPHURIC_LAKE)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
