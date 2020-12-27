package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;

public class BiomeMegalake extends EndBiome {
	public BiomeMegalake() {
		super(new BiomeDefinition("megalake")
				.setPlantsColor(73, 210, 209)
				.setFogColor(178, 209, 248)
				.setWaterAndFogColor(96, 163, 255)
				.setFogDensity(1.75F)
				.setMusic(EndSounds.MUSIC_WATER)
				.setLoop(EndSounds.AMBIENT_MEGALAKE)
				.setSurface(EndBlocks.END_MOSS, EndBlocks.ENDSTONE_DUST)
				.addStructureFeature(EndStructures.MEGALAKE)
				.addFeature(EndFeatures.END_LOTUS)
				.addFeature(EndFeatures.END_LOTUS_LEAF)
				.addFeature(EndFeatures.BUBBLE_CORAL_RARE)
				.addFeature(EndFeatures.END_LILY_RARE)
				.addFeature(EndFeatures.UMBRELLA_MOSS)
				.addFeature(EndFeatures.CREEPING_MOSS)
				.addFeature(EndFeatures.CHARNIA_CYAN)
				.addFeature(EndFeatures.CHARNIA_LIGHT_BLUE)
				.addFeature(EndFeatures.CHARNIA_RED_RARE)
				.addFeature(EndFeatures.MENGER_SPONGE)
				.addMobSpawn(EndEntities.DRAGONFLY, 50, 1, 3)
				.addMobSpawn(EndEntities.END_FISH, 50, 3, 8)
				.addMobSpawn(EndEntities.CUBOZOA, 50, 3, 8)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
