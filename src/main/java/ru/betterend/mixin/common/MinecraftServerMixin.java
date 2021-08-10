package ru.betterend.mixin.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	private ServerResources resources;
	
	@Final
	@Shadow
	private Map<ResourceKey<Level>, ServerLevel> levels;
	
	@Final
	@Shadow
	protected WorldData worldData;
	
	@Inject(method = "overworld", at = @At(value = "HEAD"), cancellable = true)
	private void be_overworld(CallbackInfoReturnable<ServerLevel> info) {
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
	private void be_createLevels(ChunkProgressListener worldGenerationProgressListener, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			ServerLevel world = levels.get(Level.END);
			if (world == null) {
				world = levels.get(Level.OVERWORLD);
			}
			this.getPlayerList().setLevel(world);
			ServerLevelData serverWorldProperties = worldData.overworldData();
			net.minecraft.world.level.levelgen.WorldGenSettings generatorOptions = worldData.worldGenSettings();
			boolean bl = generatorOptions.isDebug();
			setInitialSpawn(world, serverWorldProperties, generatorOptions.generateBonusChest(), bl);
		}
	}
	
	@Shadow
	private static void setInitialSpawn(ServerLevel serverLevel, ServerLevelData serverLevelData, boolean bl, boolean bl2) {}
	
	@Inject(method = "setInitialSpawn", at = @At(value = "HEAD"), cancellable = true)
	private static void be_setInitialSpawn(ServerLevel world, ServerLevelData serverWorldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd() && world.dimension() == Level.OVERWORLD) {
			info.cancel();
		}
		if (GeneratorOptions.changeSpawn() && world.dimension() == Level.END) {
			world.setDefaultSpawnPos(GeneratorOptions.getSpawn(), 0F);
			info.cancel();
		}
	}
	
	
	@Shadow
	public PlayerList getPlayerList() {
		return null;
	}
}
