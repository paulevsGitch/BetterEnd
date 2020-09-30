package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.GenerationSettings;
<<<<<<< HEAD
import net.minecraft.world.gen.GenerationStep;

import ru.betterend.registry.FeatureRegistry;
=======
>>>>>>> 9dcf8c34a09f9ca40aa61ba17f5142561e464dbf

@Mixin(DefaultBiomeCreator.class)
public abstract class DefaultBiomeCreatorMixin {
	@Inject(method = "composeEndSpawnSettings(Lnet/minecraft/world/biome/GenerationSettings$Builder;)Lnet/minecraft/world/biome/Biome;", at = @At("HEAD"))
	private static void addEndOres(GenerationSettings.Builder builder, CallbackInfoReturnable<Biome> rcinfo) {
		//builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, FeatureRegistry.ENDER_ORE.getFeatureConfigured());
	}
}
