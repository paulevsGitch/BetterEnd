package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class LanternWoodsBiome extends EndBiome.Config {
	public LanternWoodsBiome() {
		super("lantern_woods");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.fogColor(189, 82, 70)
			   .fogDensity(1.1F)
			   .waterAndFogColor(171, 234, 226)
			   .plantsColor(254, 85, 57)
			   .surface(surfaceMaterial().getTopMaterial())
			   .music(EndSounds.MUSIC_FOREST)
			   .particles(EndParticles.GLOWING_SPHERE, 0.001F)
			   .feature(EndFeatures.END_LAKE_NORMAL)
			   .feature(EndFeatures.FLAMAEA)
			   .feature(EndFeatures.LUCERNIA)
			   .feature(EndFeatures.LUCERNIA_BUSH)
			   .feature(EndFeatures.FILALUX)
			   .feature(EndFeatures.AERIDIUM)
			   .feature(EndFeatures.LAMELLARIUM)
			   .feature(EndFeatures.BOLUX_MUSHROOM)
			   .feature(EndFeatures.AURANT_POLYPORE)
			   .feature(EndFeatures.POND_ANEMONE)
			   .feature(EndFeatures.CHARNIA_ORANGE)
			   .feature(EndFeatures.CHARNIA_RED)
			   .feature(EndFeatures.RUSCUS)
			   .feature(EndFeatures.RUSCUS_WOOD)
			   .structure(VANILLA_FEATURES.getEND_CITY())
			   .spawn(EntityType.ENDERMAN, 50, 1, 2);
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.RUTISCUS.defaultBlockState();
			}
		};
	}
}
