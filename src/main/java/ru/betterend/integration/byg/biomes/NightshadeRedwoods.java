package ru.betterend.integration.byg.biomes;

import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import ru.bclib.BCLib;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;

public class NightshadeRedwoods extends EndBiome.Config {
    public NightshadeRedwoods() {
        super("nightshade_redwoods");
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        Biome biome = Integrations.BYG.getBiome("nightshade_forest");
        BiomeSpecialEffects effects = biome.getSpecialEffects();

        builder.fogColor(140, 108, 47)
               .fogDensity(1.5F)
               .waterAndFogColor(55, 70, 186)
               .foliageColor(122, 17, 155)
               .particles(
                       ParticleTypes.REVERSE_PORTAL,
                       0.002F
               )
               //TODO: 1.18 surface rules
//              .setSurface(biome.getGenerationSettings()
//                               .getSurfaceBuilder()
//                               .get())
               .grassColor(48, 13, 89)
               .plantsColor(200, 125, 9)
               .feature(EndFeatures.END_LAKE_RARE)
               .feature(BYGFeatures.NIGHTSHADE_REDWOOD_TREE)
               .feature(BYGFeatures.NIGHTSHADE_MOSS_WOOD)
               .feature(BYGFeatures.NIGHTSHADE_MOSS);

        if (BCLib.isClient()) {
            SoundEvent loop = effects.getAmbientLoopSoundEvent()
                                     .get();
            SoundEvent music = effects.getBackgroundMusic()
                                      .get()
                                      .getEvent();
            SoundEvent additions = effects.getAmbientAdditionsSettings()
                                          .get()
                                          .getSoundEvent();
            SoundEvent mood = effects.getAmbientMoodSettings()
                                     .get()
                                     .getSoundEvent();
            builder.loop(loop)
                   .music(music)
                   .additions(additions)
                   .mood(mood);
        }
        biome.getGenerationSettings()
             .features()
             .forEach((list) -> {
                 list.forEach((feature) -> {
                     builder.feature(Decoration.VEGETAL_DECORATION, feature.get());
                 });
             });

        for (MobCategory group : MobCategory.values()) {
            List<SpawnerData> list = biome.getMobSettings()
                                          .getMobs(group)
                                          .unwrap();
            list.forEach((entry) -> {
                builder.spawn((EntityType<? extends Mob>) entry.type, 1, entry.minCount, entry.maxCount);
            });
        }
    }
}
