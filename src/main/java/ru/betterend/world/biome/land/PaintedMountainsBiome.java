package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class PaintedMountainsBiome extends EndBiome {
	public PaintedMountainsBiome() {
		super((BiomeDefinition) new BiomeDefinition("painted_mountains")
				.addStructureFeature(EndStructures.PAINTED_MOUNTAIN)
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterAndFogColor(192, 180, 131)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.setLoop(EndSounds.AMBIENT_DUST_WASTELANDS)
				.setSurface(EndBlocks.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
