package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;

public class BiomeMegalakeGrove extends EndBiome {
	public BiomeMegalakeGrove() {
		super(new BiomeDefinition("megalake_grove")
				.setPlantsColor(73, 210, 209)
				.setFogColor(178, 209, 248)
				.setWaterColor(96, 163, 255)
				.setWaterFogColor(96, 163, 255)
				.setFogDensity(2.0F)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.setMusic(EndSounds.MUSIC_MEGALAKE)
				.setLoop(EndSounds.AMBIENT_MEGALAKE_GROVE)
				.setSurface(EndBlocks.END_MOSS)
				.addStructureFeature(EndStructures.MEGALAKE)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addFeature(EndFeatures.LACUGROVE)
				.addFeature(EndFeatures.END_LOTUS)
				.addFeature(EndFeatures.END_LOTUS_LEAF)
				.addFeature(EndFeatures.BUBBLE_CORAL_RARE)
				.addFeature(EndFeatures.END_LILY_RARE)
				.addFeature(EndFeatures.UMBRELLA_MOSS)
				.addFeature(EndFeatures.CREEPING_MOSS)
				.addMobSpawn(EndEntities.DRAGONFLY, 20, 1, 3)
				.addMobSpawn(EndEntities.END_FISH, 20, 3, 8)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
