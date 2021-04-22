package ru.betterend.mixin.common;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.betterend.util.TagHelper;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
	@Shadow
	private String name;

	@ModifyArg(method = "prepare", at = @At(value = "INVOKE",
			target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
	public Supplier<Map<ResourceLocation, Tag.Builder>> be_modifyTags(Supplier<Map<ResourceLocation, Tag.Builder>> supplier, Executor executor) {
		Map<ResourceLocation, Tag.Builder> map = supplier.get();
		TagHelper.apply(name, map);
		return () -> map;
	}
}
