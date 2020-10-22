package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;

import ru.betterend.interfaces.TeleportingEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements TeleportingEntity {
	
	private BlockPos beExitPos;
	private long beCooldown;
	
	@Shadow
	private float syncedHealth;
	@Shadow
	private int syncedExperience;
	@Shadow
	private int syncedFoodLevel;
	@Shadow
	private boolean inTeleportationState;
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	@Final
	@Shadow
	public MinecraftServer server;
	@Final
	@Shadow
	public ServerPlayerInteractionManager interactionManager;
	
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	@Shadow
	public abstract ServerWorld getServerWorld();
	
	@Shadow
	protected abstract void worldChanged(ServerWorld origin);
	
	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
		ServerWorld serverWorld = this.getServerWorld();
		RegistryKey<World> registryKey = serverWorld.getRegistryKey();
		if (beExitPos != null && registryKey == World.END && destination.getRegistryKey() == World.OVERWORLD) {
			this.inTeleportationState = true;
			ServerPlayerEntity player = ServerPlayerEntity.class.cast(this);
			WorldProperties worldProperties = destination.getLevelProperties();
			this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(destination.getDimension(), destination.getRegistryKey(), BiomeAccess.hashSeed(destination.getSeed()), this.interactionManager.getGameMode(), this.interactionManager.getPreviousGameMode(), destination.isDebugWorld(), destination.isFlat(), true));
			this.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
			PlayerManager playerManager = this.server.getPlayerManager();
			playerManager.sendCommandTree(player);
			serverWorld.removePlayer(player);
			this.removed = false;
			TeleportTarget teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				serverWorld.getProfiler().push("placing");
				this.setWorld(destination);
				destination.onPlayerChangeDimension(player);
				this.setRotation(teleportTarget.yaw, teleportTarget.pitch);
				this.refreshPositionAfterTeleport(teleportTarget.position.x, teleportTarget.position.y, teleportTarget.position.z);
				serverWorld.getProfiler().pop();
				this.worldChanged(serverWorld);
				this.interactionManager.setWorld(destination);
				this.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.abilities));
				playerManager.sendWorldInfo(player, destination);
				playerManager.sendPlayerStatus(player);
				this.getStatusEffects().forEach(statusEffectInstance -> {
					this.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(getEntityId(), statusEffectInstance));
				});
				this.networkHandler.sendPacket(new WorldEventS2CPacket(1032, BlockPos.ORIGIN, 0, false));
				this.syncedExperience = -1;
				this.syncedHealth = -1.0F;
				this.syncedFoodLevel = -1;
				info.setReturnValue(player);
				info.cancel();
			}
		}
	}

	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beExitPos != null) {
			info.setReturnValue(new TeleportTarget(new Vec3d(beExitPos.getX() + 0.5D, beExitPos.getY(), beExitPos.getZ() + 0.5D), getVelocity(), yaw, pitch));
			info.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void baseTick(CallbackInfo info) {
		if (hasCooldown()) {
			this.beCooldown--;
		}
	}
	
	@Override
	public long beGetCooldown() {
		return this.beCooldown;
	}

	@Override
	public void beSetCooldown(long time) {
		this.beCooldown = time;
	}

	@Override
	public void beSetExitPos(BlockPos pos) {
		this.beExitPos = pos;
	}

	@Override
	public BlockPos beGetExitPos() {
		return this.beExitPos;
	}
}
