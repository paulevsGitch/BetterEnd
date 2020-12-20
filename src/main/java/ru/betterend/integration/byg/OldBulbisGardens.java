package ru.betterend.integration.byg;

import java.util.List;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.BiomeDefinition;
import ru.betterend.world.biome.EndBiome;

public class OldBulbisGardens extends EndBiome {
	public OldBulbisGardens() {
		super(makeDef());
	}
	
	private static BiomeDefinition makeDef() {
		Biome biome = Integrations.BYG.getBiome("bulbis_gardens");
		SoundEvent loop = biome.getLoopSound().get();
		SoundEvent music = biome.getMusic().get().getSound();
		SoundEvent additions = biome.getAdditionsSound().get().getSound();
		SoundEvent mood = biome.getMoodSound().get().getSound();
		
		BiomeDefinition def = new BiomeDefinition("old_bulbis_gardens")
				.setFogColor(215, 132, 182)
				.setFogDensity(1.8F)
				.setWaterAndFogColor(40, 0, 56)
				.setFoliageColor(122, 17, 155)
				.setParticles(ParticleTypes.REVERSE_PORTAL, 0.002F)
				.setSurface(Integrations.BYG.getBlock("ivis_phylium"))
				.setLoop(loop)
				.setMusic(music)
				.setAdditions(additions)
				.setMood(mood)
				.addFeature(EndFeatures.END_LAKE_RARE)
				.addFeature(BYGFeatures.OLD_BULBIS_TREE)
				.addFeature(Feature.VEGETAL_DECORATION, BYGFeatures.BULBIS_TREES)
				.addFeature(Feature.VEGETAL_DECORATION, BYGFeatures.PURPLE_BULBIS_TREES)
				.addFeature(EndFeatures.PURPLE_POLYPORE)
				.addFeature(BYGFeatures.IVIS_MOSS_WOOD)
				.addFeature(BYGFeatures.IVIS_MOSS)
				.addFeature(BYGFeatures.IVIS_VINE)
				.addFeature(BYGFeatures.IVIS_SPROUT)
				.addFeature(BYGFeatures.BULBIS_ODDITY)
				.addFeature(BYGFeatures.PURPLE_BULBIS_ODDITY);
		
		for (SpawnGroup group: SpawnGroup.values()) {
			List<SpawnEntry> list = biome.getSpawnSettings().getSpawnEntry(group);
			list.forEach((entry) -> {
				def.addMobSpawn(entry);
			});
		}
		
		return def;
	}
}
