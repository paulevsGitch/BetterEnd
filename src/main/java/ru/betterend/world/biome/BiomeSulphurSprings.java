package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.world.surface.SurfaceBuilders;

public class BiomeSulphurSprings extends EndBiome {
	public BiomeSulphurSprings() {
		super(new BiomeDefinition("sulfur_springs")
				.setSurface(SurfaceBuilders.SULPHURIC_SURFACE)
				.setWaterColor(25, 90, 157)
				.setWaterFogColor(30, 65, 61)
				.setFogColor(207, 194, 62)
				.setFogDensity(1.5F)
				.setCaves(false)
				.setParticles(EndParticles.SULPHUR_PARTICLE, 0.001F)
				.addFeature(EndFeatures.GEYSER)
				.addFeature(EndFeatures.SULPHURIC_LAKE)
				.addFeature(EndFeatures.SULPHURIC_CAVE)
				.addFeature(EndFeatures.HYDRALUX)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
