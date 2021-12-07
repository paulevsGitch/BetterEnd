package ru.betterend.world.biome.land;

import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class UmbraValleyBiome extends EndBiome.Config {
	public UmbraValleyBiome() {
		super("umbra_valley");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.fogColor(100, 100, 100)
			   .plantsColor(172, 189, 190)
			   .waterAndFogColor(69, 104, 134)
			   //TODO: 1.18 surface Rules
			   //.surface(SurfaceBuilders.UMBRA_SURFACE.configured(SurfaceBuilders.DEFAULT_END_CONFIG))
			   .particles(EndParticles.AMBER_SPHERE, 0.0001F)
			   .loop(EndSounds.UMBRA_VALLEY)
			   .music(EndSounds.MUSIC_DARK)
			   .feature(EndFeatures.UMBRALITH_ARCH)
			   .feature(EndFeatures.THIN_UMBRALITH_ARCH)
			   .feature(EndFeatures.INFLEXIA)
			   .feature(EndFeatures.FLAMMALIX);
	}
}
