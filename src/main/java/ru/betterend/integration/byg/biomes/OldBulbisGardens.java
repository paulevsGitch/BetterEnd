package ru.betterend.integration.byg.biomes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import ru.bclib.BCLib;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;

import java.util.List;
import java.util.function.Supplier;

class FeaturesAccesor extends Features {
	static ConfiguredDecorator<?> shadowHEIGHTMAP_SQUARE;

	static {
		shadowHEIGHTMAP_SQUARE = Decorators.HEIGHTMAP_SQUARE;
	}
}

public class OldBulbisGardens extends EndBiome {
	public OldBulbisGardens() {
		super(makeDef());
	}

	private static BCLBiomeDef makeDef() {
		Biome biome = Integrations.BYG.getBiome("bulbis_gardens");
		BiomeSpecialEffects effects = biome.getSpecialEffects();

		Block ivis = Integrations.BYG.getBlock("ivis_phylium");
		Block origin = biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial().getBlock();
		BCLBiomeDef def = new BCLBiomeDef(BetterEnd.makeID("old_bulbis_gardens"))
				.setFogColor(215, 132, 207)
				.setFogDensity(1.8F)
				.setWaterAndFogColor(40, 0, 56)
				.setFoliageColor(122, 17, 155)
				.setParticles(ParticleTypes.REVERSE_PORTAL, 0.002F)
				.setSurface(ivis, origin)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(BYGFeatures.OLD_BULBIS_TREE);

		if (BCLib.isClient()) {
			SoundEvent loop = effects.getAmbientLoopSoundEvent().get();
			SoundEvent music = effects.getBackgroundMusic().get().getEvent();
			SoundEvent additions = effects.getAmbientAdditionsSettings().get().getSoundEvent();
			SoundEvent mood = effects.getAmbientMoodSettings().get().getSoundEvent();
			def.setLoop(loop).setMusic(music).setAdditions(additions).setMood(mood);
		}

		for (MobCategory group : MobCategory.values()) {
			List<SpawnerData> list = biome.getMobSettings().getMobs(group).unwrap();
			list.forEach((entry) -> {
				def.addMobSpawn(entry);
			});
		}

		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().features();
		List<Supplier<ConfiguredFeature<?, ?>>> vegetal = features.get(Decoration.VEGETAL_DECORATION.ordinal());
		if (vegetal.size() > 2) {
			Supplier<ConfiguredFeature<?, ?>> getter;
			// Trees (first two features)
			// I couldn't process them with conditions, so that's why they are hardcoded (paulevs)
			for (int i = 0; i < 2; i++) {
				getter = vegetal.get(i);
				ConfiguredFeature<?, ?> feature = getter.get();
				ResourceLocation id = BetterEnd.makeID("obg_feature_" + i);
				feature = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.decorated(FeaturesAccesor.shadowHEIGHTMAP_SQUARE).countRandom(1));
				def.addFeature(Decoration.VEGETAL_DECORATION, feature);
			}
			// Grasses and other features
			for (int i = 2; i < vegetal.size(); i++) {
				getter = vegetal.get(i);
				ConfiguredFeature<?, ?> feature = getter.get();
				def.addFeature(Decoration.VEGETAL_DECORATION, feature);
			}
		}

		def.addFeature(EndFeatures.PURPLE_POLYPORE)
				.addFeature(BYGFeatures.IVIS_MOSS_WOOD)
				.addFeature(BYGFeatures.IVIS_MOSS)
				.addFeature(BYGFeatures.IVIS_VINE)
				.addFeature(BYGFeatures.IVIS_SPROUT);

		return def;
	}
}
