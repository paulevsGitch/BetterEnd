package ru.betterend.mixin.common;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import ru.betterend.recipe.EndRecipeManager;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private ServerResourceManager serverResourceManager;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"), cancellable = true)
	private void beOnReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		beInjectRecipes();
	}

	@Inject(method = "loadWorld", at = @At(value = "RETURN"), cancellable = true)
	private void beOnLoadWorld(CallbackInfo info) {
		beInjectRecipes();
	}

	private void beInjectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(EndRecipeManager.getMap(accessor.getRecipes()));
		}
	}
}
