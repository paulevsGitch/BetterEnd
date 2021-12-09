package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.surface.SulphuricSurfaceNoiseCondition;

public class SulphurSpringsBiome extends EndBiome.Config {
	public SulphurSpringsBiome() {
		super("sulphur_springs");
	}

	@Override
	protected boolean hasCaves() {
		return false;
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder
			   //TODO: 1.18 check surface Rules
			   //.surface(SurfaceBuilders.SULPHURIC_SURFACE.configured(SurfaceBuilders.DEFAULT_END_CONFIG))
				.surface(
					SurfaceRules.sequence(
						SurfaceRules.ifTrue(new SulphuricSurfaceNoiseCondition(-0.6), SurfaceRules.state(surfaceMaterial().getAltTopMaterial())),
						SurfaceRules.ifTrue(new SulphuricSurfaceNoiseCondition(-0.3), SurfaceRules.state(surfaceMaterial().getTopMaterial())),
						SurfaceRules.ifTrue(new SulphuricSurfaceNoiseCondition(0.5), SULPHURIC_ROCK),
						BRIMSTONE
				))
			   .music(EndSounds.MUSIC_OPENSPACE)
			   .loop(EndSounds.AMBIENT_SULPHUR_SPRINGS)
			   .waterColor(25, 90, 157)
			   .waterFogColor(30, 65, 61)
			   .fogColor(207, 194, 62)
			   .fogDensity(1.5F)
			   //TODO: 1.18 removed
			   //.depth(0F)
			   .particles(EndParticles.SULPHUR_PARTICLE, 0.001F)
			   .feature(EndFeatures.GEYSER)
			   .feature(EndFeatures.SURFACE_VENT)
			   .feature(EndFeatures.SULPHURIC_LAKE)
			   .feature(EndFeatures.SULPHURIC_CAVE)
			   .feature(EndFeatures.HYDRALUX)
			   .feature(EndFeatures.CHARNIA_GREEN)
			   .feature(EndFeatures.CHARNIA_ORANGE)
			   .feature(EndFeatures.CHARNIA_RED_RARE)
			   .spawn(EndEntities.END_FISH, 50, 3, 8)
			   .spawn(EndEntities.CUBOZOA, 50, 3, 8)
			   .spawn(EntityType.ENDERMAN, 50, 1, 4);
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.FLAVOLITE.stone.defaultBlockState();
			}

			@Override
			public BlockState getAltTopMaterial() {
				return Blocks.END_STONE.defaultBlockState();
			}
		};
	}
}
