package ru.betterend.world.biome.cave;

import java.util.function.BiFunction;

import net.minecraft.resources.ResourceLocation;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndFeatures;
import ru.betterend.world.biome.EndBiome;

public class EmptyEndCaveBiome extends EndCaveBiome.Config {
	public static class Biome extends EndCaveBiome {
		public Biome(ResourceLocation biomeID, net.minecraft.world.level.biome.Biome biome) {
			super(biomeID, biome);

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

	public EmptyEndCaveBiome() {
		super("empty_end_cave");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		super.addCustomBuildData(builder);
		builder.fogDensity(2.0F);
	}

	@Override
	public BiFunction<ResourceLocation, net.minecraft.world.level.biome.Biome, EndBiome> getSupplier() {
		return Biome::new;
	}
}
