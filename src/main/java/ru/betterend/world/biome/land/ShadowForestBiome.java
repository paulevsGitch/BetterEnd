package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class ShadowForestBiome extends EndBiome.Config {
	public ShadowForestBiome() {
		super("shadow_forest");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.fogColor(0, 0, 0)
			   .fogDensity(2.5F)
			   .plantsColor(45, 45, 45)
			   .waterAndFogColor(42, 45, 80)
			   .particles(ParticleTypes.MYCELIUM, 0.01F)
			   .loop(EndSounds.AMBIENT_CHORUS_FOREST)
			   .music(EndSounds.MUSIC_DARK)
			   .feature(EndFeatures.VIOLECITE_LAYER)
			   .feature(EndFeatures.END_LAKE_RARE)
			   .feature(EndFeatures.DRAGON_TREE)
			   .feature(EndFeatures.DRAGON_TREE_BUSH)
			   .feature(EndFeatures.SHADOW_PLANT)
			   .feature(EndFeatures.MURKWEED)
			   .feature(EndFeatures.NEEDLEGRASS)
			   .feature(EndFeatures.SHADOW_BERRY)
			   .feature(EndFeatures.TWISTED_VINE)
			   .feature(EndFeatures.PURPLE_POLYPORE)
			   .feature(EndFeatures.TAIL_MOSS)
			   .feature(EndFeatures.TAIL_MOSS_WOOD)
			   .feature(EndFeatures.CHARNIA_PURPLE)
			   .feature(EndFeatures.CHARNIA_RED_RARE)
			   .structure(VANILLA_FEATURES.getEND_CITY())
			   .spawn(EndEntities.SHADOW_WALKER, 80, 2, 4)
			   .spawn(EntityType.ENDERMAN, 40, 1, 4)
			   .spawn(EntityType.PHANTOM, 1, 1, 2);
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.SHADOW_GRASS.defaultBlockState();
			}
		};
	}
}
