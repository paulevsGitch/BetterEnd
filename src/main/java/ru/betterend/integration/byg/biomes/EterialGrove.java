package ru.betterend.integration.byg.biomes;

import java.util.List;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.integration.byg.features.BYGFeatures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class EterialGrove extends EndBiome {
	public EterialGrove() {
		super(makeDef());
	}
	
	private static BiomeDefinition makeDef() {
		Biome biome = Integrations.BYG.getBiome("ethereal_islands");
		BiomeSpecialEffects effects = biome.getSpecialEffects();
		
		BiomeDefinition def = (BiomeDefinition) new BiomeDefinition("eterial_grove")
				.setSurface(biome.getGenerationSettings().getSurfaceBuilder().get())
				.addFeature(BYGFeatures.BIG_ETHER_TREE);
		
		if (BetterEnd.isClient()) {
			SoundEvent loop = effects.getAmbientLoopSoundEvent().get();
			SoundEvent music = effects.getBackgroundMusic().get().getEvent();
			SoundEvent additions = effects.getAmbientAdditionsSettings().get().getSoundEvent();
			SoundEvent mood = effects.getAmbientMoodSettings().get().getSoundEvent();
			def.setLoop(loop).setMusic(music).setAdditions(additions).setMood(mood);
		}
		
		for (MobCategory group: MobCategory.values()) {
			List<SpawnerData> list = biome.getMobSettings().getMobs(group);
			list.forEach((entry) -> {
				def.addMobSpawn(entry);
			});
		}
		
		return def;
	}
}
