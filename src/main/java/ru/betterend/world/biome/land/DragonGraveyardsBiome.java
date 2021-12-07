package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class DragonGraveyardsBiome extends EndBiome.Config {
	public DragonGraveyardsBiome() {
		super("dragon_graveyards");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.genChance(0.1f)
			   .fogColor(244, 46, 79)
			   .fogDensity(1.3F)
			   .particles(EndParticles.FIREFLY, 0.0007F)
			   .music(EndSounds.MUSIC_OPENSPACE)
			   .loop(EndSounds.AMBIENT_GLOWING_GRASSLANDS)
			   .surface(EndBlocks.SANGNUM)
			   .waterAndFogColor(203, 59, 167)
			   .plantsColor(244, 46, 79)
			   .feature(EndFeatures.OBSIDIAN_PILLAR_BASEMENT)
			   .feature(EndFeatures.DRAGON_BONE_BLOCK_ORE)
			   .feature(EndFeatures.FALLEN_PILLAR)
			   .feature(EndFeatures.OBSIDIAN_BOULDER)
			   .feature(EndFeatures.GIGANTIC_AMARANITA)
			   .feature(EndFeatures.LARGE_AMARANITA)
			   .feature(EndFeatures.SMALL_AMARANITA)
			   .feature(EndFeatures.GLOBULAGUS)
			   .feature(EndFeatures.CLAWFERN)
			   .spawn(EntityType.ENDERMAN, 50, 1, 2);
	}
}
