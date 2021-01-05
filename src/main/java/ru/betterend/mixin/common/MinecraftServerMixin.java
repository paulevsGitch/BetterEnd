package ru.betterend.mixin.common;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.registry.EndBiomes;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private ServerResourceManager serverResourceManager;
	
	@Final
	@Shadow
	private Map<RegistryKey<World>, ServerWorld> worlds;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"), cancellable = true)
	private void beOnReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		beInjectRecipes();
	}

	@Inject(method = "loadWorld", at = @At(value = "RETURN"), cancellable = true)
	private void beOnLoadWorld(CallbackInfo info) {
		beInjectRecipes();
		EndBiomes.initRegistry((MinecraftServer) (Object) this);
	}
	
	@Inject(method = "getOverworld", at = @At(value = "HEAD"), cancellable = true)
	private final void beGetOverworld(CallbackInfoReturnable<ServerWorld> info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerWorld world = worlds.get(World.END);
			if (world == null) {
				world = worlds.get(World.OVERWORLD);
			}
			info.setReturnValue(world);
			info.cancel();
		}
	}

	private void beInjectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(EndRecipeManager.getMap(accessor.getRecipes()));
		}
	}
}
