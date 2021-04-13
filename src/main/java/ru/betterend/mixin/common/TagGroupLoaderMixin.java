package ru.betterend.mixin.common;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.betterend.util.TagHelper;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
	
	@Shadow
	@Final
	private String entryType;

	@Inject(
		method = "method_18243", // first lambda inside prepareReload
		at = @At("RETURN"),
		locals = LocalCapture.CAPTURE_FAILHARD,
		remap = false
	)
	public void be_prepareReload(ResourceManager rm, CallbackInfoReturnable<Map<Identifier, Tag.Builder>> ci, Map<Identifier, Tag.Builder> map) {
		// executed by a worker thread
		TagHelper.apply(entryType, map);
	}
}
