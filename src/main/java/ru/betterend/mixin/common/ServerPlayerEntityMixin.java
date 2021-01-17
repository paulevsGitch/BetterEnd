package ru.betterend.mixin.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.interfaces.TeleportingEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements TeleportingEntity {

	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	@Final
	@Shadow
	public ServerPlayerInteractionManager interactionManager;
	@Final
	@Shadow
	public MinecraftServer server;
	@Shadow
	private boolean inTeleportationState;
	@Shadow
	private float syncedHealth;
	@Shadow
	private int syncedFoodLevel;
	@Shadow
	private int syncedExperience;

	private BlockPos exitPos;

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "createEndSpawnPlatform", at = @At("HEAD"), cancellable = true)
	private void be_createEndSpawnPlatform(ServerWorld world, BlockPos centerPos, CallbackInfo info) {
		if (!centerPos.equals(world.getSpawnPos()) || beCanTeleport()) {
			info.cancel();
		}
	}

	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void be_getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beCanTeleport()) {
			info.setReturnValue(new TeleportTarget(new Vec3d(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getVelocity(), yaw, pitch));
		}
	}

	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void be_moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
		if (beCanTeleport() && world instanceof ServerWorld) {
			this.inTeleportationState = true;
			ServerWorld serverWorld = this.getServerWorld();
			WorldProperties worldProperties = destination.getLevelProperties();
			ServerPlayerEntity player = ServerPlayerEntity.class.cast(this);
			this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(destination.getDimension(), destination.getRegistryKey(), BiomeAccess.hashSeed(destination.getSeed()),
					interactionManager.getGameMode(),interactionManager.getPreviousGameMode(), destination.isDebugWorld(), destination.isFlat(), true));
			this.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
			PlayerManager playerManager = this.server.getPlayerManager();
			playerManager.sendCommandTree(player);
			serverWorld.removePlayer(player);
			this.removed = false;
			TeleportTarget teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				serverWorld.getProfiler().push("moving");
				serverWorld.getProfiler().pop();
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

				for (StatusEffectInstance statusEffectInstance : this.getStatusEffects()) {
					this.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(getEntityId(), statusEffectInstance));
				}

				this.networkHandler.sendPacket(new WorldEventS2CPacket(1032, BlockPos.ORIGIN, 0, false));
				this.syncedExperience = -1;
				this.syncedHealth = -1.0F;
				this.syncedFoodLevel = -1;
			}
			this.beResetExitPos();
			info.setReturnValue(player);
		}
	}

	@Shadow
	abstract ServerWorld getServerWorld();

	@Shadow
	abstract void worldChanged(ServerWorld origin);

	@Shadow
	@Override
	protected abstract TeleportTarget getTeleportTarget(ServerWorld destination);

	@Override
	public void beSetExitPos(BlockPos pos) {
		this.exitPos = pos.toImmutable();
	}

	@Override
	public void beResetExitPos() {
		this.exitPos = null;
	}

	@Override
	public boolean beCanTeleport() {
		return this.exitPos != null;
	}
}
