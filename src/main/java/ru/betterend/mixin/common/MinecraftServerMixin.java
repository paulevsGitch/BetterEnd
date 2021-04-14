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
	private ServerResources resources;
	
	@Final
	@Shadow
	private Map<ResourceKey<Level>, ServerLevel> levels;
	
	@Final
	@Shadow
	protected WorldData worldData;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"), cancellable = true)
	private void be_reloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		be_injectRecipes();
	}

	@Inject(method = "loadLevel", at = @At(value = "RETURN"), cancellable = true)
	private void be_loadLevel(CallbackInfo info) {
		be_injectRecipes();
		EndBiomes.initRegistry((MinecraftServer) (Object) this);
	}
	
	@Inject(method = "overworld", at = @At(value = "HEAD"), cancellable = true)
	private final void be_overworld(CallbackInfoReturnable<ServerLevel> info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = levels.get(Level.END);
			if (world == null) {
				world = levels.get(Level.OVERWORLD);
			}
			info.setReturnValue(world);
			info.cancel();
		}
	}
	
	@Inject(method = "createLevels", at = @At(value = "TAIL"))
	private final void be_createLevels(ChunkProgressListener worldGenerationProgressListener, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = levels.get(Level.END);
			if (world == null) {
				world = levels.get(Level.OVERWORLD);
			}
			this.getPlayerList().setLevel(world);
			ServerLevelData serverWorldProperties = worldData.overworldData();
			net.minecraft.world.level.levelgen.WorldGenSettings generatorOptions = worldData.worldGenSettings();
			boolean bl = generatorOptions.isDebug();
			setInitialSpawn(world, serverWorldProperties, generatorOptions.generateBonusChest(), bl, true);
		}
	}
	
	@Inject(method = "setInitialSpawn", at = @At(value = "HEAD"), cancellable = true)
	private static void be_setInitialSpawn(ServerLevel world, ServerLevelData serverWorldProperties, boolean bonusChest, boolean debugWorld, boolean bl, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd() && world.dimension() == Level.OVERWORLD) {
			info.cancel();
		}
	}
	
	@Shadow
	private static void setInitialSpawn(ServerLevel world, ServerLevelData serverWorldProperties, boolean bonusChest, boolean debugWorld, boolean bl) {}
	
	@Shadow
	public PlayerList getPlayerList() {
		return null;
	}

	private void be_injectRecipes() {
		if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) resources.getRecipeManager();
			accessor.be_setRecipes(EndRecipeManager.getMap(accessor.be_getRecipes()));
		}
	}
}
