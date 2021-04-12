package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements TeleportingEntity {

	@Shadow
	public ServerGamePacketListenerImpl networkHandler;
	@Final
	@Shadow
	public ServerPlayerGameMode interactionManager;
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
	protected void be_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<PortalInfo> info) {
		if (beCanTeleport()) {
			info.setReturnValue(new PortalInfo(new Vec3(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getDeltaMovement(), yRot, xRot));
		}
	}

	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void be_moveToWorld(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (beCanTeleport() && level instanceof ServerLevel) {
			this.inTeleportationState = true;
			ServerLevel serverWorld = this.getServerWorld();
			LevelData worldProperties = destination.getLevelData();
			ServerPlayer player = ServerPlayer.class.cast(this);
			this.networkHandler.send(new ClientboundRespawnPacket(destination.dimensionType(), destination.dimension(), BiomeManager.obfuscateSeed(destination.getSeed()),
					interactionManager.getGameModeForPlayer(),interactionManager.getPreviousGameModeForPlayer(), destination.isDebug(), destination.isFlat(), true));
			this.networkHandler.send(new ClientboundChangeDifficultyPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
			PlayerList playerManager = this.server.getPlayerList();
			playerManager.sendPlayerPermissionLevel(player);
			serverWorld.removePlayerImmediately(player);
			this.removed = false;
			PortalInfo teleportTarget = this.findDimensionEntryPoint(destination);
			if (teleportTarget != null) {
				serverWorld.getProfiler().push("moving");
				serverWorld.getProfiler().pop();
				serverWorld.getProfiler().push("placing");
				this.setLevel(destination);
				destination.addDuringPortalTeleport(player);
				this.setRot(teleportTarget.yRot, teleportTarget.xRot);
				this.moveTo(teleportTarget.pos.x, teleportTarget.pos.y, teleportTarget.pos.z);
				serverWorld.getProfiler().pop();
				this.worldChanged(serverWorld);
				this.interactionManager.setLevel(destination);
				this.networkHandler.send(new ClientboundPlayerAbilitiesPacket(this.abilities));
				playerManager.sendLevelInfo(player, destination);
				playerManager.sendAllPlayerInfo(player);

				for (MobEffectInstance statusEffectInstance : this.getActiveEffects()) {
					this.networkHandler.send(new ClientboundUpdateMobEffectPacket(getId(), statusEffectInstance));
				}

				this.networkHandler.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
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
	protected abstract PortalInfo findDimensionEntryPoint(ServerLevel destination);

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
