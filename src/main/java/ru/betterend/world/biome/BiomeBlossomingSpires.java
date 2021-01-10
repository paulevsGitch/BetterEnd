package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class BiomeBlossomingSpires extends EndBiome {
	public BiomeBlossomingSpires() {
		super(new BiomeDefinition("blossoming_spires")
				.setFogColor(241, 146, 229)
				.setFogDensity(1.7F)
				.setPlantsColor(122, 45, 122)
				.setCaves(false)
				.setSurface(EndBlocks.PINK_MOSS)
				.setMusic(EndSounds.MUSIC_FOREST)
				.setLoop(EndSounds.AMBIENT_BLOSSOMING_SPIRES)
				.addFeature(EndFeatures.SPIRE)
				.addFeature(EndFeatures.FLOATING_SPIRE)
				.addFeature(EndFeatures.TENANEA)
				.addFeature(EndFeatures.TENANEA_BUSH)
				.addFeature(EndFeatures.BULB_VINE)
				.addFeature(EndFeatures.BUSHY_GRASS)
				.addFeature(EndFeatures.BUSHY_GRASS_WG)
				.addFeature(EndFeatures.BLOSSOM_BERRY)
				.addFeature(EndFeatures.TWISTED_MOSS)
				.addFeature(EndFeatures.TWISTED_MOSS_WOOD)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
