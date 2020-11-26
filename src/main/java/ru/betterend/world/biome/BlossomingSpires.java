package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class BlossomingSpires extends EndBiome {
	public BlossomingSpires() {
		super(new BiomeDefinition("blossoming_spires")
				.setFogColor(241, 146, 229)
				.setFogDensity(2.0F)
				.setPlantsColor(122, 45, 122)
				.setSurface(EndBlocks.PINK_MOSS)
				.addFeature(EndFeatures.SPIRE)
				.addFeature(EndFeatures.FLOATING_SPIRE)
				.addFeature(EndFeatures.TENANEA)
				.addFeature(EndFeatures.TENANEA_BUSH)
				.addFeature(EndFeatures.BULB_VINE)
				.addFeature(EndFeatures.TWISTED_MOSS)
				.addFeature(EndFeatures.TWISTED_MOSS_WOOD)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}