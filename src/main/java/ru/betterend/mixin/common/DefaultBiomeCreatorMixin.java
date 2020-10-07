package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import ru.betterend.registry.DefaultBiomeFeatureRegistry;

@Mixin(DefaultBiomeCreator.class)
public class DefaultBiomeCreatorMixin {
	@Shadow
	private static Biome composeEndSpawnSettings(GenerationSettings.Builder builder) {
		return null;
	};

	@Overwrite
	public static Biome createEndHighlands() {
		GenerationSettings.Builder builder = (new GenerationSettings.Builder())
				.surfaceBuilder(ConfiguredSurfaceBuilders.END)
				//.structureFeature(ConfiguredStructureFeatures.END_CITY)
				.feature(GenerationStep.Feature.SURFACE_STRUCTURES, ConfiguredFeatures.END_GATEWAY)
				//.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.CHORUS_PLANT)
				.structureFeature(DefaultBiomeFeatureRegistry.MOUNTAINS.getFeatureConfigured());
		return composeEndSpawnSettings(builder);
	}
}
