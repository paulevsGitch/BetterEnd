package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

@Mixin(DefaultBiomeCreator.class)
public class DefaultBiomeCreatorMixin {
	@Shadow
	private static Biome composeEndSpawnSettings(GenerationSettings.Builder builder) {
		return null;
	};

	@Inject(method = "createEndHighlands", at = @At("HEAD"), cancellable = true)
	private static void createEndHighlands(CallbackInfoReturnable<Biome> info) {
		GenerationSettings.Builder builder = (new GenerationSettings.Builder())
				.surfaceBuilder(ConfiguredSurfaceBuilders.END)
				.feature(GenerationStep.Feature.SURFACE_STRUCTURES, ConfiguredFeatures.END_GATEWAY);
		info.setReturnValue(composeEndSpawnSettings(builder));
		info.cancel();
	}
}
