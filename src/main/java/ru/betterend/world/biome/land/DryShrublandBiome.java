package ru.betterend.world.biome.land;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.entity.EntityType;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class DryShrublandBiome extends EndBiome {
	public DryShrublandBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("dry_shrubland")).setFogColor(132, 35, 13).setFogDensity(1.2F).setWaterAndFogColor(113, 88, 53).setPlantsColor(237, 122, 66).setSurface(EndBlocks.RUTISCUS).setMusic(EndSounds.MUSIC_OPENSPACE).addFeature(EndFeatures.LUCERNIA_BUSH_RARE).addFeature(EndFeatures.ORANGO).addFeature(EndFeatures.AERIDIUM).addFeature(EndFeatures.LUTEBUS).addFeature(EndFeatures.LAMELLARIUM).addStructureFeature(StructureFeatures.END_CITY).addMobSpawn(EntityType.ENDERMAN, 50, 1, 2));
	}
}
