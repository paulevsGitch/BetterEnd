package ru.betterend.world.biome.cave;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.BCLBiomeBuilder.BiomeSupplier;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.bclib.world.biomes.BCLBiomeSettings;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.world.biome.EndBiome;

public class LushSmaragdantCaveBiome extends EndCaveBiome.Config {
	public static class Biome extends EndCaveBiome {
		public Biome(ResourceLocation biomeID, net.minecraft.world.level.biome.Biome biome, BCLBiomeSettings settings) {
			super(biomeID, biome, settings);

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

	public LushSmaragdantCaveBiome() {
		super("lush_smaragdant_cave");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		super.addCustomBuildData(builder);
		builder.fogColor(0, 253, 182)
			   .fogDensity(2.0F)
			   .plantsColor(0, 131, 145)
			   .waterAndFogColor(31, 167, 212)
			   .particles(EndParticles.SMARAGDANT, 0.001F);
	}

	@Override
	public BiomeSupplier<EndBiome> getSupplier() {
		return LushSmaragdantCaveBiome.Biome::new;
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.CAVE_MOSS.defaultBlockState();
			}
		};
	}
}
