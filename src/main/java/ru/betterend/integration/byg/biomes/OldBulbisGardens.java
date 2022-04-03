package ru.betterend.integration.byg.biomes;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import ru.bclib.BCLib;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;

import java.util.List;
import java.util.function.Supplier;


public class OldBulbisGardens extends EndBiome.Config {
	public OldBulbisGardens() {
		super("old_bulbis_gardens");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		Holder<Biome> biome = Integrations.BYG.getBiome("bulbis_gardens");
		BiomeSpecialEffects effects = biome.value().getSpecialEffects();

		Block ivis = Integrations.BYG.getBlock("ivis_phylium");
//		Block origin = biome.getGenerationSettings()
//							.getSurfaceBuilderConfig()
//							.getTopMaterial()
//							.getBlock();
		builder.fogColor(215, 132, 207)
			   .fogDensity(1.8F)
			   .waterAndFogColor(40, 0, 56)
			   .foliageColor(122, 17, 155)
			   .particles(
					   ParticleTypes.REVERSE_PORTAL,
					   0.002F
			   )
			   //TODO: 1.18 surface rules
			   //.surface(ivis, origin)
			   .feature(EndFeatures.END_LAKE_RARE)
			   .feature(BYGFeatures.OLD_BULBIS_TREE);

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

		for (MobCategory group : MobCategory.values()) {
			List<SpawnerData> list = biome.value()
					                      .getMobSettings()
										  .getMobs(group)
										  .unwrap();
			list.forEach((entry) -> {
				builder.spawn((EntityType<? extends Mob>) entry.type, 1, entry.minCount, entry.maxCount);
			});
		}

		List<HolderSet<PlacedFeature>> features = biome.value().getGenerationSettings()
															.features();
		HolderSet<PlacedFeature> vegetal = features.get(Decoration.VEGETAL_DECORATION.ordinal());
		if (vegetal.size() > 2) {
			Supplier<PlacedFeature> getter;
			// Trees (first two features)
			// I couldn't process them with conditions, so that's why they are hardcoded (paulevs)
			for (int i = 0; i < 2; i++) {
				getter = vegetal.get(i);
				Holder<PlacedFeature> feature = getter.get();
				ResourceLocation id = BetterEnd.makeID("obg_feature_" + i);
				feature = Registry.register(
						BuiltinRegistries.PLACED_FEATURE,
						id,
						feature
				);
				builder.feature(Decoration.VEGETAL_DECORATION, feature);
			}
			// Grasses and other features
			for (int i = 2; i < vegetal.size(); i++) {
				getter = vegetal.get(i);
				Holder<PlacedFeature> feature = getter.get();
				builder.feature(Decoration.VEGETAL_DECORATION, feature);
			}
		}

		builder.feature(EndFeatures.PURPLE_POLYPORE)
			   .feature(BYGFeatures.IVIS_MOSS_WOOD)
			   .feature(BYGFeatures.IVIS_MOSS)
			   .feature(BYGFeatures.IVIS_VINE)
			   .feature(BYGFeatures.IVIS_SPROUT);
	}
}
