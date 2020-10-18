package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.BlockRegistry;

public class BiomeDustWastelands extends EndBiome {
	public BiomeDustWastelands() {
		super(new BiomeDefinition("dust_wastelands")
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterColor(192, 180, 131)
				.setWaterFogColor(192, 180, 131)
				.setSurface(BlockRegistry.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				//.setLoop(SoundRegistry.AMBIENT_FOGGY_MUSHROOMLAND)
				//.setMusic(SoundRegistry.MUSIC_FOGGY_MUSHROOMLAND)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
