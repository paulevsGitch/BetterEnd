package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class BlossomingSpiresBiome extends EndBiome {
	public BlossomingSpiresBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("blossoming_spires"))
			.addCustomData("has_caves", false)
			.setFogColor(241, 146, 229)
			.setFogDensity(1.7F)
			.setPlantsColor(122, 45, 122)
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
			.addFeature(EndFeatures.SILK_MOTH_NEST)
			.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4)
			.addMobSpawn(EndEntities.SILK_MOTH, 5, 1, 2));
	}
}
