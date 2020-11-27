package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class BiomeShadowForest extends EndBiome {
	public BiomeShadowForest() {
		super(new BiomeDefinition("shadow_forest")
				.setFogColor(0, 0, 0)
				.setFogDensity(2.5F)
				.setPlantsColor(45, 45, 45)
				.setWaterColor(42, 45, 80)
				.setWaterFogColor(42, 45, 80)
				.setSurface(EndBlocks.SHADOW_GRASS)
				.setParticles(ParticleTypes.MYCELIUM, 0.01F)
				.setLoop(EndSounds.AMBIENT_CHORUS_FOREST)
				.setMusic(EndSounds.MUSIC_SHADOW_FOREST)
				.addFeature(EndFeatures.VIOLECITE_LAYER)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(EndFeatures.DRAGON_TREE)
				.addFeature(EndFeatures.DRAGON_TREE_BUSH)
				.addFeature(EndFeatures.SHADOW_PLANT)
				.addFeature(EndFeatures.MURKWEED)
				.addFeature(EndFeatures.NEEDLEGRASS)
				.addFeature(EndFeatures.SHADOW_BERRY)
				.addFeature(EndFeatures.TWISTED_VINE)
				.addFeature(EndFeatures.PURPLE_POLYPORE)
				.addFeature(EndFeatures.TAIL_MOSS)
				.addFeature(EndFeatures.TAIL_MOSS_WOOD)
				.addStructureFeature(ConfiguredStructureFeatures.END_CITY)
				.addMobSpawn(EndEntities.SHADOW_WALKER, 80, 2, 4)
				.addMobSpawn(EntityType.ENDERMAN, 40, 1, 4)
				.addMobSpawn(EntityType.PHANTOM, 1, 1, 2));
	}
}
