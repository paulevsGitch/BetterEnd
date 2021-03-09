package ru.betterend.integration.byg.biomes;

import java.util.List;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.world.biome.land.BiomeDefinition;
import ru.betterend.world.biome.land.EndBiome;

public class EterialGrove extends EndBiome {
	public EterialGrove() {
		super(makeDef());
	}
	
	private static BiomeDefinition makeDef() {
		Biome biome = Integrations.BYG.getBiome("ethereal_islands");
		BiomeEffects effects = biome.getEffects();
		
		BiomeDefinition def = new BiomeDefinition("eterial_grove")
				.setSurface(biome.getGenerationSettings().getSurfaceBuilder().get())
				.addFeature(BYGFeatures.BIG_ETHER_TREE);
		
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
		
		return def;
	}
}
