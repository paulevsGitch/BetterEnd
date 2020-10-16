package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.EntityRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ParticleRegistry;
import ru.betterend.registry.SoundRegistry;
import ru.betterend.registry.StructureRegistry;

public class BiomeFoggyMushroomland extends EndBiome {
	public BiomeFoggyMushroomland() {
		super(new BiomeDefinition("foggy_mushroomland")
				.setFogColor(41, 122, 173)
				.setFogDensity(3)
				.setWaterColor(119, 227, 250)
				.setWaterFogColor(119, 227, 250)
				.setSurface(BlockRegistry.END_MOSS, BlockRegistry.END_MYCELIUM)
				.setParticles(ParticleRegistry.GLOWING_SPHERE, 0.001F)
				.setLoop(SoundRegistry.AMBIENT_FOGGY_MUSHROOMLAND)
				.setMusic(SoundRegistry.MUSIC_FOGGY_MUSHROOMLAND)
				.addStructureFeature(StructureRegistry.GIANT_MOSSY_GLOWSHROOM)
				.addFeature(FeatureRegistry.ENDER_ORE)
				.addFeature(FeatureRegistry.END_LAKE)
				.addFeature(FeatureRegistry.MOSSY_GLOWSHROOM)
				.addFeature(FeatureRegistry.BLUE_VINE)
				.addFeature(FeatureRegistry.UMBRELLA_MOSS)
				.addFeature(FeatureRegistry.CREEPING_MOSS)
				.addFeature(FeatureRegistry.DENSE_VINE)
				.addFeature(FeatureRegistry.END_LILY)
				.addFeature(FeatureRegistry.BUBBLE_CORAL)
				.addMobSpawn(EntityRegistry.DRAGONFLY, 80, 2, 5)
				.addMobSpawn(EntityRegistry.END_SLIME, 10, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 10, 1, 2));
	}
}
