package ru.betterend.mixin.common;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.util.TagHelper;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
	@Shadow
	private String directory;
	
	@Inject(method = "prepare", at = @At("RETURN"), cancellable = true)
	public void be_prepareReload(ResourceManager manager, Executor executor, CallbackInfoReturnable<CompletableFuture<Map<ResourceLocation, Tag.Builder>>> info) {
		CompletableFuture<Map<ResourceLocation, Tag.Builder>> future = info.getReturnValue();
		info.setReturnValue(CompletableFuture.supplyAsync(() -> {
			Map<ResourceLocation, Tag.Builder> map = Maps.newHashMap(future.join());
			TagHelper.apply(directory, map);
			return map;
		}, executor));
	}
}