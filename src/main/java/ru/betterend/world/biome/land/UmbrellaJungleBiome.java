package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class UmbrellaJungleBiome extends EndBiome.Config {
	public UmbrellaJungleBiome() {
		super("umbrella_jungle");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.fogColor(87, 223, 221)
				.waterAndFogColor(119, 198, 253)
				.foliageColor(27, 183, 194)
				.fogDensity(2.3F)
				.particles(EndParticles.JUNGLE_SPORE, 0.001F)
				.music(EndSounds.MUSIC_FOREST)
				.loop(EndSounds.AMBIENT_UMBRELLA_JUNGLE)
			   .surface(surfaceMaterial().getTopMaterial())
				.feature(EndFeatures.END_LAKE)
				.feature(EndFeatures.UMBRELLA_TREE)
				.feature(EndFeatures.JELLYSHROOM)
				.feature(EndFeatures.TWISTED_UMBRELLA_MOSS)
				.feature(EndFeatures.SMALL_JELLYSHROOM_FLOOR)
				.feature(EndFeatures.JUNGLE_GRASS)
				.feature(EndFeatures.CYAN_MOSS)
				.feature(EndFeatures.CYAN_MOSS_WOOD)
				.feature(EndFeatures.JUNGLE_FERN_WOOD)
				.feature(EndFeatures.SMALL_JELLYSHROOM_WALL)
				.feature(EndFeatures.SMALL_JELLYSHROOM_WOOD)
				.feature(EndFeatures.SMALL_JELLYSHROOM_CEIL)
				.feature(EndFeatures.JUNGLE_VINE)
				.feature(EndFeatures.CHARNIA_CYAN)
				.feature(EndFeatures.CHARNIA_GREEN)
				.feature(EndFeatures.CHARNIA_LIGHT_BLUE)
				.feature(EndFeatures.CHARNIA_RED_RARE)
				.structure(VANILLA_FEATURES.getEND_CITY())
				.spawn(EntityType.ENDERMAN, 50, 1, 2);
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.JUNGLE_MOSS.defaultBlockState();
			}
		};
	}
}