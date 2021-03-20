package ru.betterend.integration.byg.biomes;

import java.util.List;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.biome.land.BiomeDefinition;

public class NightshadeRedwoods extends EndBiome {
	public NightshadeRedwoods() {
		super(makeDef());
	}
	
	private static BiomeDefinition makeDef() {
		Biome biome = Integrations.BYG.getBiome("nightshade_forest");
		BiomeEffects effects = biome.getEffects();
		
		BiomeDefinition def = new BiomeDefinition("nightshade_redwoods")
				.setFogColor(140, 108, 47)
				.setFogDensity(1.5F)
				.setWaterAndFogColor(55, 70, 186)
				.setFoliageColor(122, 17, 155)
				.setParticles(ParticleTypes.REVERSE_PORTAL, 0.002F)
				.setSurface(biome.getGenerationSettings().getSurfaceBuilder().get())
				.setGrassColor(48, 13, 89)
				.setPlantsColor(200, 125, 9)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(BYGFeatures.NIGHTSHADE_REDWOOD_TREE)
				.addFeature(BYGFeatures.NIGHTSHADE_MOSS_WOOD)
				.addFeature(BYGFeatures.NIGHTSHADE_MOSS);
		
		if (BetterEnd.isClient()) {
			SoundEvent loop = effects.getLoopSound().get();
			SoundEvent music = effects.getMusic().get().getSound();
			SoundEvent additions = effects.getAdditionsSound().get().getSound();
			SoundEvent mood = effects.getMoodSound().get().getSound();
			def.setLoop(loop).setMusic(music).setAdditions(additions).setMood(mood);
		}
		biome.getGenerationSettings().getFeatures().forEach((list) -> {
			list.forEach((feature) -> {
				def.addFeature(Feature.VEGETAL_DECORATION, feature.get());
			});
		});
		
		for (SpawnGroup group: SpawnGroup.values()) {
			List<SpawnEntry> list = biome.getSpawnSettings().getSpawnEntry(group);
			list.forEach((entry) -> {
				def.addMobSpawn(entry);
			});
		}
		
		return def;
	}
}
