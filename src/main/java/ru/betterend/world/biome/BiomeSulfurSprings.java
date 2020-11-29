package ru.betterend.world.biome;

import net.minecraft.entity.EntityType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

public class BiomeSulfurSprings extends EndBiome {
	public BiomeSulfurSprings() {
		super(new BiomeDefinition("sulfur_springs")
				.setSurface(EndBlocks.SULFURIC_ROCK.stone)
				.addFeature(EndFeatures.GEYSER)
				.addMobSpawn(EntityType.ENDERMAN, 50, 1, 4));
	}
}
