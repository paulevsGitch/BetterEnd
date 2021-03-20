package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.EndBiome;

public class FoggyMushroomlandBiome extends EndBiome {
	public FoggyMushroomlandBiome() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setPlantsColor(73, 210, 209)
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterAndFogColor(119, 227, 250)
				.setSurface(EndBlocks.END_MOSS, EndBlocks.END_MYCELIUM)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.setLoop(EndSounds.AMBIENT_FOGGY_MUSHROOMLAND)
				.setMusic(EndSounds.MUSIC_FOREST)
				.addStructureFeature(EndStructures.GIANT_MOSSY_GLOWSHROOM)
				.addFeature(EndFeatures.END_LAKE)
				.addFeature(EndFeatures.MOSSY_GLOWSHROOM)
				.addFeature(EndFeatures.BLUE_VINE)
				.addFeature(EndFeatures.UMBRELLA_MOSS)
				.addFeature(EndFeatures.CREEPING_MOSS)
				.addFeature(EndFeatures.DENSE_VINE)
				.addFeature(EndFeatures.PEARLBERRY)
				.addFeature(EndFeatures.CYAN_MOSS)
				.addFeature(EndFeatures.CYAN_MOSS_WOOD)
				.addFeature(EndFeatures.END_LILY)
				.addFeature(EndFeatures.BUBBLE_CORAL)
				.addFeature(EndFeatures.CHARNIA_CYAN)
				.addFeature(EndFeatures.CHARNIA_LIGHT_BLUE)
				.addFeature(EndFeatures.CHARNIA_RED_RARE)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EndEntities.DRAGONFLY, 80, 2, 5)
				.addMobSpawn(EndEntities.END_FISH, 20, 2, 5)
				.addMobSpawn(EndEntities.CUBOZOA, 10, 3, 8)
				.addMobSpawn(EndEntities.END_SLIME, 10, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
