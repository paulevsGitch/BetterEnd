package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.effect.StatusEffectInstance;
import net.minecraft.world.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import ru.betterend.client.ClientOptions;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BackgroundInfo;
import ru.betterend.world.biome.EndBiome;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	private static float lastFogDensity;
	private static float fogDensity;
	private static float lerp;
	private static long time;

	@Shadow
	private static float red;
	@Shadow
	private static float green;
	@Shadow
	private static float blue;

	@Inject(method = "render", at = @At("RETURN"))
	private static void be_onRender(Camera camera, float tickDelta, ClientLevel world, int i, float f,
			CallbackInfo info) {
		long l = Util.getMeasuringTimeMs() - time;
		time += l;
		lerp += l * 0.001F;
		if (lerp > 1)
			lerp = 1;

		FluidState fluidState = camera.getSubmergedFluidState();
		if (fluidState.isEmpty() && world.dimension().equals(Level.END)) {
			Entity entity = camera.getFocusedEntity();
			boolean skip = false;
			if (entity instanceof LivingEntity) {
				StatusEffectInstance effect = ((LivingEntity) entity).getStatusEffect(StatusEffects.NIGHT_VISION);
				skip = effect != null && effect.getDuration() > 0;
			}
			if (!skip) {
				red *= 4;
				green *= 4;
				blue *= 4;
			}
		}

		BackgroundInfo.red = red;
		BackgroundInfo.green = green;
		BackgroundInfo.blue = blue;
	}

	@Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
	private static void be_fogDensity(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance,
			boolean thickFog, CallbackInfo info) {
		Entity entity = camera.getFocusedEntity();
		Biome biome = entity.world.getBiome(entity.getBlockPos());
		FluidState fluidState = camera.getSubmergedFluidState();
		if (ClientOptions.useFogDensity() && biome.getCategory() == Category.THEEND && fluidState.isEmpty()) {
			EndBiome endBiome = EndBiomes.getRenderBiome(biome);

			if (fogDensity == 0) {
				fogDensity = endBiome.getFogDensity();
				lastFogDensity = fogDensity;
			}
			if (lerp == 1) {
				lastFogDensity = fogDensity;
				fogDensity = endBiome.getFogDensity();
				lerp = 0;
			}

			float fog = Mth.lerp(lerp, lastFogDensity, fogDensity);
			BackgroundInfo.fog = fog;
			float start = viewDistance * 0.75F / fog;
			float end = viewDistance / fog;

			if (entity instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) entity;
				StatusEffectInstance effect = le.getStatusEffect(StatusEffects.BLINDNESS);
				if (effect != null) {
					int duration = effect.getDuration();
					if (duration > 20) {
						start = 0;
						end *= 0.03F;
						BackgroundInfo.blindness = 1;
					} else {
						float delta = (float) duration / 20F;
						BackgroundInfo.blindness = delta;
						start = Mth.lerp(delta, start, 0);
						end = Mth.lerp(delta, end, end * 0.03F);
					}
				} else {
					BackgroundInfo.blindness = 0;
				}
			}

			RenderSystem.fogStart(start);
			RenderSystem.fogEnd(end);
			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
			RenderSystem.setupNvFogDistance();
			info.cancel();
		}
	}
}
