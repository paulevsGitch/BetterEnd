package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.SoundRegistry;
import ru.betterend.registry.StructureRegistry;

public class BiomePaintedMountains extends EndBiome {
	public BiomePaintedMountains() {
		super(new BiomeDefinition("painted_mountains")
				.setFogColor(226, 239, 168)
				.setFogDensity(2)
				.setWaterColor(192, 180, 131)
				.setWaterFogColor(192, 180, 131)
				.setMusic(SoundRegistry.DUST_WASTELANDS)
				.setSurface(BlockRegistry.ENDSTONE_DUST)
				.setParticles(ParticleTypes.WHITE_ASH, 0.01F)
				.addStructureFeature(StructureRegistry.PAINTED_MOUNTAIN)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
