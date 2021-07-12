package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class ShadowForestBiome extends EndBiome {
	public ShadowForestBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("shadow_forest")).setFogColor(0, 0, 0).setFogDensity(2.5F).setPlantsColor(45, 45, 45).setWaterAndFogColor(42, 45, 80).setSurface(EndBlocks.SHADOW_GRASS).setParticles(ParticleTypes.MYCELIUM, 0.01F).setLoop(EndSounds.AMBIENT_CHORUS_FOREST).setMusic(EndSounds.MUSIC_DARK).addFeature(EndFeatures.VIOLECITE_LAYER).addFeature(EndFeatures.END_LAKE_RARE).addFeature(EndFeatures.DRAGON_TREE).addFeature(EndFeatures.DRAGON_TREE_BUSH).addFeature(EndFeatures.SHADOW_PLANT).addFeature(EndFeatures.MURKWEED).addFeature(EndFeatures.NEEDLEGRASS).addFeature(EndFeatures.SHADOW_BERRY).addFeature(EndFeatures.TWISTED_VINE).addFeature(EndFeatures.PURPLE_POLYPORE).addFeature(EndFeatures.TAIL_MOSS).addFeature(EndFeatures.TAIL_MOSS_WOOD).addFeature(EndFeatures.CHARNIA_PURPLE).addFeature(EndFeatures.CHARNIA_RED_RARE).addStructureFeature(StructureFeatures.END_CITY).addMobSpawn(EndEntities.SHADOW_WALKER, 80, 2, 4).addMobSpawn(EntityType.ENDERMAN, 40, 1, 4).addMobSpawn(EntityType.PHANTOM, 1, 1, 2));
	}
}
