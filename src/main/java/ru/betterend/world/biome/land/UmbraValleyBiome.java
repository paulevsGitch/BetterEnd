package ru.betterend.world.biome.land;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.surface.UmbraSurfaceNoiseCondition;

public class UmbraValleyBiome extends EndBiome.Config {
    public UmbraValleyBiome() {
        super("umbra_valley");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        builder.fogColor(100, 100, 100)
               .plantsColor(172, 189, 190)
               .waterAndFogColor(69, 104, 134)
               //TODO: 1.18 check surface Rules
               //.surface(SurfaceBuilders.UMBRA_SURFACE.configured(SurfaceBuilders.DEFAULT_END_CONFIG))
               .surface(
                       SurfaceRules.sequence(
                               SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(
                                       SurfaceRules.ifTrue(new UmbraSurfaceNoiseCondition(0.4), SurfaceRules.state(surfaceMaterial().getAltTopMaterial())),
                                       SurfaceRules.ifTrue(new UmbraSurfaceNoiseCondition(0.15), PALLIDIUM_HEAVY),
                                       SurfaceRules.ifTrue(new UmbraSurfaceNoiseCondition(-0.15), PALLIDIUM_THIN),
                                       SurfaceRules.ifTrue(new UmbraSurfaceNoiseCondition(-0.4), PALLIDIUM_TINY)
                               )), SurfaceRules.state(surfaceMaterial().getTopMaterial())
                       )
               )
               .particles(EndParticles.AMBER_SPHERE, 0.0001F)
               .loop(EndSounds.UMBRA_VALLEY)
               .music(EndSounds.MUSIC_DARK)
               .feature(EndFeatures.UMBRALITH_ARCH)
               .feature(EndFeatures.THIN_UMBRALITH_ARCH)
               .feature(EndFeatures.INFLEXIA)
               .feature(EndFeatures.FLAMMALIX);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EndBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EndBlocks.UMBRALITH.stone.defaultBlockState();
            }

            @Override
            public BlockState getAltTopMaterial() {
                return EndBlocks.PALLIDIUM_FULL.defaultBlockState();
            }
        };
    }
}
