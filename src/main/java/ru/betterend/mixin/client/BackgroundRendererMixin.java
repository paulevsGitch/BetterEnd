package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import ru.betterend.registry.BiomeRegistry;
import ru.betterend.world.biome.EndBiome;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	private static float lastFogStart;
	private static float lastFogEnd;
	private static float fogStart;
	private static float fogEnd;
	private static float lerp;
	
	private static final float SKY_RED = 21F / 255F;
	private static final float SKY_GREEN = 16F / 255F;
	private static final float SKY_BLUE = 20F / 255F;
	//private static final float NORMAL = 0.12757292F;
	
	@Shadow
	private static float red;
	@Shadow
	private static float green;
	@Shadow
	private static float blue;
	
	@Inject(method = "render", at = @At("RETURN"))
	private static void onRender(Camera camera, float tickDelta, ClientWorld world, int i, float f, CallbackInfo info) {
		lerp += tickDelta * 0.01F;
		if (lerp > 1) lerp = 1;
		
		FluidState fluidState = camera.getSubmergedFluidState();
		if (fluidState.isEmpty() && world.getDimension().hasEnderDragonFight()) {
			RenderSystem.clearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 0);
			//red /= NORMAL;
			//green /= NORMAL;
			//blue /= NORMAL;
		}
	}
	
	@Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
	private static void fogDensity(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info) {
		ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) camera.getFocusedEntity();
		Biome biome = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
		FluidState fluidState = camera.getSubmergedFluidState();
		if (biome.getCategory() == Category.THEEND && fluidState.isEmpty()) {
			EndBiome endBiome = BiomeRegistry.getFromBiome(biome);
			
			if (fogEnd == 0) {
				fogStart = viewDistance * 0.75F / endBiome.getFogDensity();
				fogEnd = viewDistance / endBiome.getFogDensity();
				lastFogStart = fogStart;
				lastFogEnd = fogEnd;
			}
			if (lerp == 1) {
				lastFogStart = fogStart;
				lastFogEnd = fogEnd;
				fogStart = viewDistance * 0.75F / endBiome.getFogDensity();
				fogEnd = viewDistance / endBiome.getFogDensity();
				lerp = 0;
			}
			
			RenderSystem.fogStart(MathHelper.lerp(lerp, lastFogStart, fogStart));
			RenderSystem.fogEnd(MathHelper.lerp(lerp, lastFogEnd, fogEnd));
			RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
			RenderSystem.setupNvFogDistance();
			info.cancel();
		}
	}
}
