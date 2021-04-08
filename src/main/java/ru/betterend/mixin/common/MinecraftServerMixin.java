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
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerWorldProperties;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.registry.EndBiomes;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private ServerResourceManager serverResourceManager;

	@Final
	@Shadow
	private Map<RegistryKey<Level>, ServerLevel> worlds;

	@Final
	@Shadow
	protected SaveProperties saveProperties;

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
	private final void beGetOverworld(CallbackInfoReturnable<ServerLevel> info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = worlds.get(Level.END);
			if (world == null) {
				world = worlds.get(Level.OVERWORLD);
			}
			info.setReturnValue(world);
			info.cancel();
		}
	}

	@Inject(method = "createWorlds", at = @At(value = "TAIL"))
	private final void be_CreateWorlds(WorldGenerationProgressListener worldGenerationProgressListener,
			CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = worlds.get(Level.END);
			if (world == null) {
				world = worlds.get(Level.OVERWORLD);
			}
			this.getPlayerManager().setMainWorld(world);
			ServerWorldProperties serverWorldProperties = saveProperties.getMainWorldProperties();
			net.minecraft.world.gen.GeneratorOptions generatorOptions = saveProperties.getGeneratorOptions();
			boolean bl = generatorOptions.isDebugWorld();
			setupSpawn(world, serverWorldProperties, generatorOptions.hasBonusChest(), bl, true);
		}
	}

	@Inject(method = "setupSpawn", at = @At(value = "HEAD"), cancellable = true)
	private static void be_SetupSpawn(ServerLevel world, ServerWorldProperties serverWorldProperties,
			boolean bonusChest, boolean debugWorld, boolean bl, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd() && world.dimension() == Level.OVERWORLD) {
			info.cancel();
		}
	}

	@Shadow
	private static void setupSpawn(ServerLevel world, ServerWorldProperties serverWorldProperties, boolean bonusChest,
			boolean debugWorld, boolean bl) {
	}

	@Shadow
	public PlayerManager getPlayerManager() {
		return null;
	}

	private void beInjectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(EndRecipeManager.getMap(accessor.getRecipes()));
		}
	}
}
