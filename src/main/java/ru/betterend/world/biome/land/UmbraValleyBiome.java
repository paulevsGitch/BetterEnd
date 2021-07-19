package ru.betterend.world.biome.land;

import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.surface.SurfaceBuilders;

public class UmbraValleyBiome extends EndBiome {
	public UmbraValleyBiome() {
		super(
			new BCLBiomeDef(BetterEnd.makeID("umbra_valley"))
			.setFogColor(100, 100, 100)
			.setPlantsColor(200, 200, 200)
			.setWaterAndFogColor(69, 104, 134)
			.setSurface(SurfaceBuilders.UMBRA_SURFACE.configured(SurfaceBuilders.DEFAULT_END_CONFIG))
		);
	}
}
