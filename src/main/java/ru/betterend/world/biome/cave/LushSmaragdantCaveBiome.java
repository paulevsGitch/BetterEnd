package ru.betterend.world.biome.cave;

import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;

public class LushSmaragdantCaveBiome extends EndCaveBiome {
	public LushSmaragdantCaveBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("lush_smaragdant_cave")).setFogColor(0, 253, 182)
																	   .setFogDensity(2.0F)
																	   .setPlantsColor(0, 131, 145)
																	   .setWaterAndFogColor(31, 167, 212)
																	   .setParticles(EndParticles.SMARAGDANT, 0.001F)
																	   .setSurface(EndBlocks.CAVE_MOSS));
		
		this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL, 1);
		this.addFloorFeature(EndFeatures.SMARAGDANT_CRYSTAL_SHARD, 20);
		
		this.addCeilFeature(EndFeatures.END_STONE_STALACTITE, 1);
	}
	
	@Override
	public float getFloorDensity() {
		return 0.1F;
	}
	
	@Override
	public float getCeilDensity() {
		return 0.1F;
	}
}
