package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class BiomeShadowForest extends EndBiome {
	public BiomeShadowForest() {
		super(new BiomeDefinition("shadow_forest")
				.setFogColor(87, 26, 87)
				.setFogDensity(1.5F)
				.setPlantsColor(122, 45, 122)
				.setSurface(EndBlocks.SHADOW_GRASS)
				.setParticles(ParticleTypes.MYCELIUM, 0.01F)
				//.setLoop(EndSounds.AMBIENT_CHORUS_FOREST)
				//.setMusic(EndSounds.MUSIC_CHORUS_FOREST)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.DRAGON_TREE)
				//.addFeature(EndFeatures.PYTHADENDRON_BUSH)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EntityType.ENDERMAN, 80, 1, 4));
	}
}
