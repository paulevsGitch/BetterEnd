package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.EntityRegistry;
import ru.betterend.registry.FeatureRegistry;

public class BiomeChorusForest extends EndBiome {
	public BiomeChorusForest() {
		super(new BiomeDefinition("chorus_forest")
				.setFogColor(87, 26, 87)
				.setFogDensity(1.5F)
				.setSurface(BlockRegistry.CHORUS_NYLIUM)
				//.setParticles(ParticleRegistry.GLOWING_SPHERE, 0.001F)
				//.setLoop(SoundRegistry.AMBIENT_FOGGY_MUSHROOMLAND)
				//.setMusic(SoundRegistry.MUSIC_FOGGY_MUSHROOMLAND)
				.addFeature(FeatureRegistry.ENDER_ORE)
				.addFeature(FeatureRegistry.RARE_END_LAKE)
				.addFeature(FeatureRegistry.PYTHADENDRON_TREE)
				.addFeature(FeatureRegistry.CHORUS_GRASS)
				.addMobSpawn(EntityRegistry.END_SLIME, 5, 1, 2)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
