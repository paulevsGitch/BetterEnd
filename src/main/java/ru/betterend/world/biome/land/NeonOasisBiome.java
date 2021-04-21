package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class NeonOasisBiome extends EndBiome {
	public NeonOasisBiome() {
		super(new BiomeDefinition("neon_oasis")
				.setGenChance(0.5F)
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterAndFogColor(106, 238, 215)
				.setSurface(EndBlocks.ENDSTONE_DUST, EndBlocks.END_MOSS)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.setLoop(EndSounds.AMBIENT_DUST_WASTELANDS)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.addFeature(EndFeatures.DESERT_LAKE)
				.addFeature(EndFeatures.NEON_CACTUS)
				.addFeature(EndFeatures.UMBRELLA_MOSS)
				.addFeature(EndFeatures.CREEPING_MOSS)
				.addStructureFeature(StructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
