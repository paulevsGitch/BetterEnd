package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityMobEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements TeleportingEntity {

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

	public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "createEndSpawnPlatform", at = @At("HEAD"), cancellable = true)
	private void be_createEndSpawnPlatform(ServerLevel world, BlockPos centerPos, CallbackInfo info) {
		if (!GeneratorOptions.generateObsidianPlatform()) {
			info.cancel();
		}
	}

	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void be_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beCanTeleport()) {
			info.setReturnValue(new TeleportTarget(
					new Vec3d(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getVelocity(), yaw, pitch));
		}
	}

	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void be_moveToWorld(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (beCanTeleport() && world instanceof ServerLevel) {
			this.inTeleportationState = true;
			ServerLevel serverWorld = this.getServerWorld();
			WorldProperties worldProperties = destination.getLevelProperties();
			ServerPlayer player = ServerPlayer.class.cast(this);
			this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(destination.getDimension(),
					destination.dimension(), BiomeAccess.hashSeed(destination.getSeed()),
					interactionManager.getGameMode(), interactionManager.getPreviousGameMode(),
					destination.isDebugWorld(), destination.isFlat(), true));
			this.networkHandler.sendPacket(
					new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
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
				this.refreshPositionAfterTeleport(teleportTarget.position.x, teleportTarget.position.y,
						teleportTarget.position.z);
				serverWorld.getProfiler().pop();
				this.worldChanged(serverWorld);
				this.interactionManager.setWorld(destination);
				this.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.abilities));
				playerManager.sendWorldInfo(player, destination);
				playerManager.sendPlayerStatus(player);

				for (MobEffectInstance statusEffectInstance : this.getMobEffects()) {
					this.networkHandler.sendPacket(new EntityMobEffectS2CPacket(getEntityId(), statusEffectInstance));
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
	abstract ServerLevel getServerWorld();

	@Shadow
	abstract void worldChanged(ServerLevel origin);

	@Shadow
	@Override
	protected abstract TeleportTarget getTeleportTarget(ServerLevel destination);

	@Override
	public void beSetExitPos(BlockPos pos) {
		this.exitPos = pos.immutable();
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
