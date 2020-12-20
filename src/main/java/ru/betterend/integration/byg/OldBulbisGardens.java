package ru.betterend.integration.byg;

import net.minecraft.particle.ParticleTypes;
import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class OldBulbisGardens extends EndBiome {
	public OldBulbisGardens() {
		super(new BiomeDefinition("old_bulbis_gardens")
				.setFogColor(132, 0, 202)
				.setFogDensity(2F)
				.setWaterAndFogColor(40, 0, 56)
				.setParticles(ParticleTypes.REVERSE_PORTAL, 0.002F)
				.setSurface(Integrations.BYG.getBlock("ivis_phylium"))
				.addFeature(BYGFeatures.OLD_BULBIS_TREE)
				.addFeature(EndFeatures.PURPLE_POLYPORE)
				.addFeature(BYGFeatures.IVIS_MOSS_WOOD)
				.addFeature(BYGFeatures.IVIS_MOSS)
				.addFeature(BYGFeatures.IVIS_VINE)
				.addFeature(BYGFeatures.IVIS_SPROUT));
	}
}
