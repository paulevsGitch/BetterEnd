package ru.betterend.world.biome.land;

import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.surface.SurfaceBuilders;

public class UmbraValleyBiome extends EndBiome {
	public UmbraValleyBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("umbra_valley"))
			.setFogColor(100, 100, 100)
			.setPlantsColor(172, 189, 190)
			.setWaterAndFogColor(69, 104, 134)
			.setSurface(SurfaceBuilders.UMBRA_SURFACE.configured(SurfaceBuilders.DEFAULT_END_CONFIG))
			.setParticles(EndParticles.AMBER_SPHERE, 0.0001F)
			.setLoop(EndSounds.UMBRA_VALLEY)
			.setMusic(EndSounds.MUSIC_DARK)
			.addFeature(EndFeatures.UMBRALITH_ARCH)
			.addFeature(EndFeatures.THIN_UMBRALITH_ARCH)
			.addFeature(EndFeatures.INFLEXIA)
			.addFeature(EndFeatures.FLAMMALIX)
		);
	}
}
