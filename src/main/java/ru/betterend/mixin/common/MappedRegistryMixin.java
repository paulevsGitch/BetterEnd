package ru.betterend.mixin.common;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
	// TODO Make this a part of BCLib (implement froze/unfroze methods)
	@Inject(method = "validateWrite", at = @At("HEAD"), cancellable = true)
	private void be_validateWrite(ResourceKey<T> resourceKey, CallbackInfo info) {
		info.cancel();
	}
}
