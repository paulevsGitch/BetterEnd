package ru.betterend.world.biome.land;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class DragonGraveyardsBiome extends EndBiome {
	public DragonGraveyardsBiome() {
		super(new BiomeDefinition("dragon_graveyards")
				.setGenChance(0.1F)
				.setFogColor(244, 46, 79)
				.setFogDensity(1.3F)
				.setParticles(EndParticles.FIREFLY, 0.0007F)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.setLoop(EndSounds.AMBIENT_GLOWING_GRASSLANDS)
				.setSurface(EndBlocks.SANGNUM)
				.setWaterAndFogColor(203, 59, 167)
				.setPlantsColor(244, 46, 79)
				.addFeature(EndFeatures.OBSIDIAN_PILLAR_BASEMENT)
				.addFeature(EndFeatures.FALLEN_PILLAR)
				.addFeature(EndFeatures.OBSIDIAN_BOULDER)
				.addFeature(EndFeatures.GIGANTIC_AMARANITA)
				.addFeature(EndFeatures.LARGE_AMARANITA)
				.addFeature(EndFeatures.SMALL_AMARANITA)
				.addFeature(EndFeatures.GLOBULAGUS)
				.addFeature(EndFeatures.CLAWFERN)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
