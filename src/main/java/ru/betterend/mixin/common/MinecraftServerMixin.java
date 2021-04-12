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
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import ru.betterend.recipe.EndRecipeManager;
import ru.betterend.registry.EndBiomes;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private ServerResources serverResourceManager;
	
	@Final
	@Shadow
	private Map<ResourceKey<Level>, ServerLevel> worlds;
	
	@Final
	@Shadow
	protected WorldData saveProperties;

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
	private final void be_CreateWorlds(ChunkProgressListener worldGenerationProgressListener, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = worlds.get(Level.END);
			if (world == null) {
				world = worlds.get(Level.OVERWORLD);
			}
			this.getPlayerManager().setLevel(world);
			ServerLevelData serverWorldProperties = saveProperties.overworldData();
			net.minecraft.world.level.levelgen.WorldGenSettings generatorOptions = saveProperties.worldGenSettings();
			boolean bl = generatorOptions.isDebug();
			setupSpawn(world, serverWorldProperties, generatorOptions.generateBonusChest(), bl, true);
		}
	}
	
	@Inject(method = "setupSpawn", at = @At(value = "HEAD"), cancellable = true)
	private static void be_SetupSpawn(ServerLevel world, ServerLevelData serverWorldProperties, boolean bonusChest, boolean debugWorld, boolean bl, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd() && world.dimension() == Level.OVERWORLD) {
			info.cancel();
		}
	}
	
	@Shadow
	private static void setupSpawn(ServerLevel world, ServerLevelData serverWorldProperties, boolean bonusChest, boolean debugWorld, boolean bl) {}
	
	@Shadow
	public PlayerList getPlayerManager() {
		return null;
	}

	private void beInjectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(EndRecipeManager.getMap(accessor.getRecipes()));
		}
	}
}
