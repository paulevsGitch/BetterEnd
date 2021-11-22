package ru.betterend.mixin.client;

import net.minecraft.client.renderer.DimensionSpecialEffects.EndEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.integration.Integrations;

@Mixin(value = EndEffects.class, priority = 10)
public class EndEffectsMixin {
	@Inject(method = "getBrightnessDependentFogColor", at = @At("HEAD"), cancellable = true)
	private void be_restoreBrightness(Vec3 color, float sunHeight, CallbackInfoReturnable<Vec3> info) {
		if (Integrations.ENDERSCAPE.modIsInstalled()) {
			info.setReturnValue(color.scale(0.15000000596046448D));
		}
	}
	
	@Inject(method = "isFoggyAt", at = @At("HEAD"), cancellable = true)
	private void be_restoreFog(int camX, int camY, CallbackInfoReturnable<Boolean> info) {
		if (Integrations.ENDERSCAPE.modIsInstalled()) {
			info.setReturnValue(false);
		}
	}
}
