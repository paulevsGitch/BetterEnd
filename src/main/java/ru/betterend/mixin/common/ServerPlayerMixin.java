package ru.betterend.mixin.common;

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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
	private int be_teleportDelay = 0;
	
	public ServerPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	@Inject(method = "createEndPlatform", at = @At("HEAD"), cancellable = true)
	private void be_createEndSpawnPlatform(ServerLevel world, BlockPos centerPos, CallbackInfo info) {
		if (!GeneratorOptions.generateObsidianPlatform()) {
			info.cancel();
		}
	}
	
	@Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
	protected void be_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<PortalInfo> info) {
		if (be_canTeleport()) {
			info.setReturnValue(new PortalInfo(
				new Vec3(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5),
				getDeltaMovement(),
				getYRot(),
				getXRot()
			));
		}
		else if (GeneratorOptions.changeSpawn() && destination.dimension() == Level.END) {
			BlockPos spawn = GeneratorOptions.getSpawn();
			Vec3 pos = new Vec3(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
			info.setReturnValue(new PortalInfo(pos, Vec3.ZERO, 90.0F, 0.0F));
		}
	}
	
	@Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
	public void be_changeDimension(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (be_canTeleport() && level instanceof ServerLevel) {
			isChangingDimension = true;
			ServerLevel serverWorld = getLevel();
			LevelData worldProperties = destination.getLevelData();
			ServerPlayer player = ServerPlayer.class.cast(this);
			connection.send(new ClientboundRespawnPacket(
				destination.dimensionType(),
				destination.dimension(),
				BiomeManager.obfuscateSeed(destination.getSeed()),
				gameMode.getGameModeForPlayer(),
				gameMode.getPreviousGameModeForPlayer(),
				destination.isDebug(),
				destination.isFlat(),
				true
			));
			connection.send(new ClientboundChangeDifficultyPacket(
				worldProperties.getDifficulty(),
				worldProperties.isDifficultyLocked()
			));
			PlayerList playerManager = server.getPlayerList();
			playerManager.sendPlayerPermissionLevel(player);
			serverWorld.removePlayerImmediately(player, RemovalReason.CHANGED_DIMENSION);
			unsetRemoved();
			PortalInfo teleportTarget = findDimensionEntryPoint(destination);
			if (teleportTarget != null) {
				serverWorld.getProfiler().push("moving");
				serverWorld.getProfiler().pop();
				serverWorld.getProfiler().push("placing");
				this.level = destination;
				destination.addDuringPortalTeleport(player);
				setRot(teleportTarget.yRot, teleportTarget.xRot);
				moveTo(teleportTarget.pos.x, teleportTarget.pos.y, teleportTarget.pos.z);
				serverWorld.getProfiler().pop();
				triggerDimensionChangeTriggers(serverWorld);
				gameMode.setLevel(destination);
				connection.send(new ClientboundPlayerAbilitiesPacket(getAbilities()));
				playerManager.sendLevelInfo(player, destination);
				playerManager.sendAllPlayerInfo(player);
				
				for (MobEffectInstance statusEffectInstance : getActiveEffects()) {
					connection.send(new ClientboundUpdateMobEffectPacket(getId(), statusEffectInstance));
				}
				
				connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				lastSentExp = -1;
				lastSentHealth = -1.0F;
				lastSentFood = -1;
			}
			be_teleportDelay = 100;
			be_resetExitPos();
			info.setReturnValue(player);
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void be_decreaseCooldawn(CallbackInfo info) {
		if (be_teleportDelay > 0) be_teleportDelay--;
	}
	
	@Override
	public int getDimensionChangingDelay() {
		if (be_teleportDelay > 0) {
			return be_teleportDelay;
		}
		return super.getDimensionChangingDelay();
	}
	
	@Shadow
	public abstract ServerLevel getLevel();
	
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
