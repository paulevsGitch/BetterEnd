package ru.betterend.world.biome.cave;

import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndFeatures;

public class EmptyEndCaveBiome extends EndCaveBiome {
	public EmptyEndCaveBiome() {
		super(new BCLBiomeDef(BetterEnd.makeID("empty_end_cave")).setFogDensity(2.0F));
		this.addFloorFeature(EndFeatures.END_STONE_STALAGMITE, 1);
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
