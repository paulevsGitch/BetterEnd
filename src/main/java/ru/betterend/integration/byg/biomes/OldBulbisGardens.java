package ru.betterend.integration.byg.biomes;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.biome.land.BiomeDefinition;

public class OldBulbisGardens extends EndBiome {
	public OldBulbisGardens() {
		super(makeDef());
	}
	
	private static BiomeDefinition makeDef() {
		Biome biome = Integrations.BYG.getBiome("bulbis_gardens");
		BiomeEffects effects = biome.getEffects();
		
		Block ivis = Integrations.BYG.getBlock("ivis_phylium");
		Block origin = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial().getBlock();
		BiomeDefinition def = new BiomeDefinition("old_bulbis_gardens")
				.setFogColor(215, 132, 207)
				.setFogDensity(1.8F)
				.setWaterAndFogColor(40, 0, 56)
				.setFoliageColor(122, 17, 155)
				.setParticles(ParticleTypes.REVERSE_PORTAL, 0.002F)
				.setSurface(ivis, origin)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(BYGFeatures.OLD_BULBIS_TREE);
		
		if (BetterEnd.isClient()) {
			SoundEvent loop = effects.getLoopSound().get();
			SoundEvent music = effects.getMusic().get().getSound();
			SoundEvent additions = effects.getAdditionsSound().get().getSound();
			SoundEvent mood = effects.getMoodSound().get().getSound();
			def.setLoop(loop).setMusic(music).setAdditions(additions).setMood(mood);
		}
		
		for (SpawnGroup group: SpawnGroup.values()) {
			List<SpawnEntry> list = biome.getSpawnSettings().getSpawnEntry(group);
			list.forEach((entry) -> {
				def.addMobSpawn(entry);
			});
		}
		
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();
		List<Supplier<ConfiguredFeature<?, ?>>> vegetal = features.get(Feature.VEGETAL_DECORATION.ordinal());
		if (vegetal.size() > 2) {
			Supplier<ConfiguredFeature<?, ?>> getter;
			// Trees (first two features)
			// I couldn't process them with conditions, so that's why they are hardcoded (paulevs)
			for (int i = 0; i < 2; i++) {
				getter = vegetal.get(i);
				ConfiguredFeature<?, ?> feature = getter.get();
				Identifier id = BetterEnd.makeID("obg_feature_" + i);
				feature = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(1));
				def.addFeature(Feature.VEGETAL_DECORATION, feature);
			}
			// Grasses and other features
			for (int i = 2; i < vegetal.size(); i++) {
				getter = vegetal.get(i);
				ConfiguredFeature<?, ?> feature = getter.get();
				def.addFeature(Feature.VEGETAL_DECORATION, feature);
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
