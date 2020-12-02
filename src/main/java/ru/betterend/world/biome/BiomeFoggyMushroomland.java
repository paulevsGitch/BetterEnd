package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setPlantsColor(73, 210, 209)
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterAndFogColor(119, 227, 250)
				.setSurface(EndBlocks.END_MOSS, EndBlocks.END_MYCELIUM)
				.setParticles(EndParticles.GLOWING_SPHERE, 0.001F)
				.setLoop(EndSounds.AMBIENT_FOGGY_MUSHROOMLAND)
				.setMusic(EndSounds.MUSIC_FOGGY_MUSHROOMLAND)
				.addStructureFeature(EndStructures.GIANT_MOSSY_GLOWSHROOM)
				.addFeature(EndFeatures.END_LAKE)
				.addFeature(EndFeatures.MOSSY_GLOWSHROOM)
				.addFeature(EndFeatures.BLUE_VINE)
				.addFeature(EndFeatures.UMBRELLA_MOSS)
				.addFeature(EndFeatures.CREEPING_MOSS)
				.addFeature(EndFeatures.DENSE_VINE)
				.addFeature(EndFeatures.CYAN_MOSS)
				.addFeature(EndFeatures.CYAN_MOSS_WOOD)
				.addFeature(EndFeatures.END_LILY)
				.addFeature(EndFeatures.BUBBLE_CORAL)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EndEntities.DRAGONFLY, 80, 2, 5)
				.addMobSpawn(EndEntities.END_FISH, 20, 2, 5)
				.addMobSpawn(EndEntities.END_SLIME, 10, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
