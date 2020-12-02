package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;

public class BiomePaintedMountains extends EndBiome {
	public BiomePaintedMountains() {
		super(new BiomeDefinition("painted_mountains")
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setCaves(false)
				.setWaterColor(192, 180, 131)
				.setWaterFogColor(192, 180, 131)
				.setMusic(EndSounds.MUSIC_DUST_WASTELANDS)
				.setLoop(EndSounds.AMBIENT_DUST_WASTELANDS)
				.setSurface(EndBlocks.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.addStructureFeature(EndStructures.PAINTED_MOUNTAIN)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
