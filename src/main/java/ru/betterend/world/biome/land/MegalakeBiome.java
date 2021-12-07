package ru.betterend.world.biome.land;

import net.minecraft.world.entity.EntityType;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.biome.EndBiome;

public class MegalakeBiome extends EndBiome.Config {
	public MegalakeBiome() {
		super("megalake");
	}

	@Override
	protected void addCustomBuildData(BCLBiomeBuilder builder) {
		builder.structure(EndStructures.MEGALAKE.getFeatureConfigured())
			   .plantsColor(73, 210, 209)
			   .fogColor(178, 209, 248)
			   .waterAndFogColor(96, 163, 255)
			   .fogDensity(1.75F)
			   .music(EndSounds.MUSIC_WATER)
			   .loop(EndSounds.AMBIENT_MEGALAKE)
			   //TODO: 1.18 check surface Rules
			   .chancedSurface(END_MOSS, ENDSTONE_DUST, END_STONE)
			   //.surface(EndBlocks.END_MOSS, EndBlocks.ENDSTONE_DUST)
			   //TODO: 1.18 removed
			   //.depth(0F)
			   .feature(EndFeatures.END_LOTUS)
			   .feature(EndFeatures.END_LOTUS_LEAF)
			   .feature(EndFeatures.BUBBLE_CORAL_RARE)
			   .feature(EndFeatures.END_LILY_RARE)
			   .feature(EndFeatures.UMBRELLA_MOSS)
			   .feature(EndFeatures.CREEPING_MOSS)
			   //.feature(EndFeatures.PEARLBERRY)
			   .feature(EndFeatures.CHARNIA_CYAN)
			   .feature(EndFeatures.CHARNIA_LIGHT_BLUE)
			   .feature(EndFeatures.CHARNIA_RED_RARE)
			   .feature(EndFeatures.MENGER_SPONGE)
			   .spawn(EndEntities.DRAGONFLY, 50, 1, 3)
			   .spawn(EndEntities.END_FISH, 50, 3, 8)
			   .spawn(EndEntities.CUBOZOA, 50, 3, 8)
			   .spawn(EndEntities.END_SLIME, 5, 1, 2)
			   .spawn(EntityType.ENDERMAN, 10, 1, 2);
	}
}
