package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.interfaces.SurfaceMaterialProvider;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class DustWastelandsBiome extends EndBiome.Config {
	public DustWastelandsBiome() {
		super("dust_wastelands");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder
			.fogColor(226, 239, 168)
			.fogDensity(2)
			.waterAndFogColor(192, 180, 131)
			.terrainHeight(1.5F)
			.particles(ParticleTypes.WHITE_ASH, 0.01F)
			.loop(EndSounds.AMBIENT_DUST_WASTELANDS)
			.music(EndSounds.MUSIC_OPENSPACE)
			.structure(BiomeTags.HAS_END_CITY)
			.spawn(EntityType.ENDERMAN, 50, 1, 2);
	}

	@Override
	protected SurfaceMaterialProvider surfaceMaterial() {
		return new EndBiome.DefaultSurfaceMaterialProvider() {
			@Override
			public BlockState getTopMaterial() {
				return EndBlocks.ENDSTONE_DUST.defaultBlockState();
			}
			
			@Override
			public SurfaceRuleBuilder surface() {
				return super
					.surface()
					.ceil(Blocks.END_STONE.defaultBlockState())
					.rule(4, SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(5, false, CaveSurface.FLOOR),
						SurfaceRules.state(EndBlocks.ENDSTONE_DUST.defaultBlockState())
					));
			}
		};
	}
}
