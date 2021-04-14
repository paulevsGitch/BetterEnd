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
public abstract class ServerPlayerMixin extends Player implements TeleportingEntity {
	@Shadow
	public ServerGamePacketListenerImpl connection;
	@Final
	@Shadow
	public ServerPlayerGameMode gameMode;
	@Final
	@Shadow
	public MinecraftServer server;
	@Shadow
	private boolean isChangingDimension;
	@Shadow
	private float lastSentHealth;
	@Shadow
	private int lastSentFood;
	@Shadow
	private int lastSentExp;

	private BlockPos exitPos;

	public ServerPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
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
		if (be_canTeleport()) {
			info.setReturnValue(new PortalInfo(new Vec3(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getDeltaMovement(), yRot, xRot));
		}
	}

	@Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
	public void be_changeDimension(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (be_canTeleport() && level instanceof ServerLevel) {
			this.isChangingDimension = true;
			ServerLevel serverWorld = this.getLevel();
			LevelData worldProperties = destination.getLevelData();
			ServerPlayer player = ServerPlayer.class.cast(this);
			this.connection.send(new ClientboundRespawnPacket(destination.dimensionType(), destination.dimension(), BiomeManager.obfuscateSeed(destination.getSeed()),
					gameMode.getGameModeForPlayer(),gameMode.getPreviousGameModeForPlayer(), destination.isDebug(), destination.isFlat(), true));
			this.connection.send(new ClientboundChangeDifficultyPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
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
				this.triggerDimensionChangeTriggers(serverWorld);
				this.gameMode.setLevel(destination);
				this.connection.send(new ClientboundPlayerAbilitiesPacket(this.abilities));
				playerManager.sendLevelInfo(player, destination);
				playerManager.sendAllPlayerInfo(player);

				for (MobEffectInstance statusEffectInstance : this.getActiveEffects()) {
					this.connection.send(new ClientboundUpdateMobEffectPacket(getId(), statusEffectInstance));
				}

				this.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				this.lastSentExp = -1;
				this.lastSentHealth = -1.0F;
				this.lastSentFood = -1;
			}
			this.be_resetExitPos();
			info.setReturnValue(player);
		}
	}

	@Shadow
	abstract ServerLevel getLevel();

	@Shadow
	abstract void triggerDimensionChangeTriggers(ServerLevel origin);

	@Shadow
	@Override
	protected abstract PortalInfo findDimensionEntryPoint(ServerLevel destination);

	@Override
	public void be_setExitPos(BlockPos pos) {
		this.exitPos = pos.immutable();
	}

	@Override
	public void be_resetExitPos() {
		this.exitPos = null;
	}

	@Override
	public boolean be_canTeleport() {
		return this.exitPos != null;
	}
}
