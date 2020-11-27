package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;

public class BiomeDustWastelands extends EndBiome {
	public BiomeDustWastelands() {
		super(new BiomeDefinition("dust_wastelands")
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterColor(192, 180, 131)
				.setWaterFogColor(192, 180, 131)
				.setSurface(EndBlocks.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.setLoop(EndSounds.AMBIENT_DUST_WASTELANDS)
				.setMusic(EndSounds.MUSIC_DUST_WASTELANDS)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
