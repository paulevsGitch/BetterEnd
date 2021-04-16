package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.material.FluidState;
import ru.betterend.client.ClientOptions;
import ru.betterend.registry.EndBiomes;
import ru.betterend.util.BackgroundInfo;
import ru.betterend.world.biome.EndBiome;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
	private static float lastFogDensity;
	private static float fogDensity;
	private static float lerp;
	private static long time;
	
	@Shadow
	private static float fogRed;
	@Shadow
	private static float fogGreen;
	@Shadow
	private static float fogBlue;
	
	@Inject(method = "setupColor", at = @At("RETURN"))
	private static void be_onRender(Camera camera, float tickDelta, ClientLevel world, int i, float f, CallbackInfo info) {
		long l = Util.getMillis() - time;
		time += l;
		lerp += l * 0.001F;
		if (lerp > 1) lerp = 1;
		
		FluidState fluidState = camera.getFluidInCamera();
		if (fluidState.isEmpty() && world.dimension().equals(Level.END)) {
			Entity entity = camera.getEntity();
			boolean skip = false;
			if (entity instanceof LivingEntity) {
				MobEffectInstance effect = ((LivingEntity) entity).getEffect(MobEffects.NIGHT_VISION);
				skip = effect != null && effect.getDuration() > 0;
			}
			if (!skip) {
				fogRed *= 4;
				fogGreen *= 4;
				fogBlue *= 4;
			}
		}
		
		BackgroundInfo.red = fogRed;
		BackgroundInfo.green = fogGreen;
		BackgroundInfo.blue = fogBlue;
	}
	
	@Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
	private static void be_fogDensity(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
		Entity entity = camera.getEntity();
		Biome biome = entity.level.getBiome(entity.blockPosition());
		FluidState fluidState = camera.getFluidInCamera();
		if (ClientOptions.useFogDensity() && biome.getBiomeCategory() == BiomeCategory.THEEND && fluidState.isEmpty()) {
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
				MobEffectInstance effect = le.getEffect(MobEffects.BLINDNESS);
				if (effect != null) {
					int duration = effect.getDuration();
					if (duration > 20) {
						start = 0;
						end *= 0.03F;
						BackgroundInfo.blindness = 1;
					}
					else {
						float delta = (float) duration / 20F;
						BackgroundInfo.blindness = delta;
						start = Mth.lerp(delta, start, 0);
						end = Mth.lerp(delta, end, end * 0.03F);
					}
				}
				else {
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
