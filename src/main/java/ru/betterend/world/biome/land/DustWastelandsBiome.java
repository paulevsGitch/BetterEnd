package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class DustWastelandsBiome extends EndBiome {
	public DustWastelandsBiome() {
		super(new BiomeDefinition("dust_wastelands")
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterAndFogColor(192, 180, 131)
				.setSurface(EndBlocks.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.setLoop(EndSounds.AMBIENT_DUST_WASTELANDS)
				.setMusic(EndSounds.MUSIC_OPENSPACE)
				.addStructureFeature(StructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
