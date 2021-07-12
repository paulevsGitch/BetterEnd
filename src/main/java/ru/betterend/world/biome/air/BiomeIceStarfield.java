package ru.betterend.world.biome.air;

import net.minecraft.world.entity.EntityType;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.EndBiome;

public class BiomeIceStarfield extends EndBiome {
	public BiomeIceStarfield() {
		super(new BCLBiomeDef(BetterEnd.makeID("ice_starfield")).addCustomData("has_caves", false).addStructureFeature(EndStructures.GIANT_ICE_STAR.getFeatureConfigured()).setFogColor(224, 245, 254).setTemperature(0F).setFogDensity(2.2F).setFoliageColor(193, 244, 244).setGenChance(0.25F).setParticles(EndParticles.SNOWFLAKE, 0.002F).addFeature(EndFeatures.ICE_STAR).addFeature(EndFeatures.ICE_STAR_SMALL).addMobSpawn(EntityType.ENDERMAN, 20, 1, 4));
	}
}
