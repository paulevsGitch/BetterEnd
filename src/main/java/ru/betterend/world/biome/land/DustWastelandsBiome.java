package ru.betterend.world.biome.land;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;

public class DustWastelandsBiome extends EndBiome.Config {
	public DustWastelandsBiome() {
		super("dust_wastelands");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.fogColor(226, 239, 168)
			   .fogDensity(2)
			   .waterAndFogColor(192, 180, 131)
			   .surface(EndBlocks.ENDSTONE_DUST)
			   //TODO: 1.18 removed
			   //.depth(1.5F)
			   .particles(ParticleTypes.WHITE_ASH, 0.01F)
			   .loop(EndSounds.AMBIENT_DUST_WASTELANDS)
			   .music(EndSounds.MUSIC_OPENSPACE)
			   .structure(VANILLA_FEATURES.getEND_CITY())
			   .spawn(EntityType.ENDERMAN, 50, 1, 2);
	}
}
