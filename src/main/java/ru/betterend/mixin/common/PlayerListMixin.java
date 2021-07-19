package ru.betterend.mixin.common;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Final
	@Shadow
	private static Logger LOGGER;
	
	@Final
	@Shadow
	private MinecraftServer server;
	
	@Final
	@Shadow
	private RegistryAccess.RegistryHolder registryHolder;
	
	@Shadow
	private int viewDistance;
	
	@Final
	@Shadow
	private List<ServerPlayer> players;
	
	@Final
	@Shadow
	private Map<UUID, ServerPlayer> playersByUUID;
	
	@Inject(method = "placeNewPlayer", at = @At(value = "HEAD"), cancellable = true)
	public void be_placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			GameProfile gameProfile = serverPlayer.getGameProfile();
			GameProfileCache userCache = this.server.getProfileCache();
			Optional<GameProfile> gameProfile2 = userCache.get(gameProfile.getId());
			String string = gameProfile2.isPresent() ? gameProfile2.get().getName() : gameProfile.getName();
			userCache.add(gameProfile);
			CompoundTag compoundTag = this.load(serverPlayer);
			ResourceKey<Level> var23;
			if (compoundTag != null) {
				DataResult<ResourceKey<Level>> var10000 = DimensionType.parseLegacy(new Dynamic<Tag>(
					NbtOps.INSTANCE,
					compoundTag.get("Dimension")
				));
				Logger var10001 = LOGGER;
				Objects.requireNonNull(var10001);
				var23 = (ResourceKey<Level>) var10000.resultOrPartial(var10001::error).orElse(Level.END);
			}
			else {
				var23 = Level.END;
			}
			
			ResourceKey<Level> registryKey = var23;
			ServerLevel serverLevel = this.server.getLevel(registryKey);
			ServerLevel serverLevel3;
			if (serverLevel == null) {
				LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", registryKey);
				serverLevel3 = this.server.overworld();
			}
			else {
				serverLevel3 = serverLevel;
			}
			
			serverPlayer.setLevel(serverLevel3);
			//serverPlayer.gameMode.setLevel((ServerLevel) serverPlayer.level);
			String string2 = "local";
			if (connection.getRemoteAddress() != null) {
				string2 = connection.getRemoteAddress().toString();
			}
			
			LOGGER.info(
				"{}[{}] logged in with entity id {} at ({}, {}, {})",
				serverPlayer.getName().getString(),
				string2,
				serverPlayer.getId(),
				serverPlayer.getX(),
				serverPlayer.getY(),
				serverPlayer.getZ()
			);
			LevelData worldProperties = serverLevel3.getLevelData();
			serverPlayer.loadGameTypes(compoundTag);
			//this.updatePlayerGameMode(serverPlayer, (ServerPlayer) null, serverLevel3);
			ServerGamePacketListenerImpl serverPlayNetworkHandler = new ServerGamePacketListenerImpl(
				this.server,
				connection,
				serverPlayer
			);
			GameRules gameRules = serverLevel3.getGameRules();
			boolean bl = gameRules.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
			boolean bl2 = gameRules.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
			serverPlayNetworkHandler.send(new ClientboundLoginPacket(
				serverPlayer.getId(),
				serverPlayer.gameMode.getGameModeForPlayer(),
				serverPlayer.gameMode.getPreviousGameModeForPlayer(),
				BiomeManager.obfuscateSeed(serverLevel3.getSeed()),
				worldProperties.isHardcore(),
				this.server.levelKeys(),
				this.registryHolder,
				serverLevel3.dimensionType(),
				serverLevel3.dimension(),
				this.getMaxPlayers(),
				this.viewDistance,
				bl2,
				!bl,
				serverLevel3.isDebug(),
				serverLevel3.isFlat()
			));
			serverPlayNetworkHandler.send(new ClientboundCustomPayloadPacket(
				ClientboundCustomPayloadPacket.BRAND,
				(new FriendlyByteBuf(Unpooled.buffer())).writeUtf(this.getServer().getServerModName())
			));
			serverPlayNetworkHandler.send(new ClientboundChangeDifficultyPacket(
				worldProperties.getDifficulty(),
				worldProperties.isDifficultyLocked()
			));
			serverPlayNetworkHandler.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
			serverPlayNetworkHandler.send(new ClientboundSetCarriedItemPacket(serverPlayer.getInventory().selected));
			serverPlayNetworkHandler.send(new ClientboundUpdateRecipesPacket(this.server.getRecipeManager()
																						.getRecipes()));
			serverPlayNetworkHandler.send(new ClientboundUpdateTagsPacket(this.server.getTags()
																					 .serializeToNetwork(this.registryHolder)));
			this.sendPlayerPermissionLevel(serverPlayer);
			serverPlayer.getStats().markAllDirty();
			serverPlayer.getRecipeBook().sendInitialRecipeBook(serverPlayer);
			this.updateEntireScoreboard(serverLevel3.getScoreboard(), serverPlayer);
			this.server.invalidateStatus();
			TranslatableComponent mutableText2;
			if (serverPlayer.getGameProfile().getName().equalsIgnoreCase(string)) {
				mutableText2 = new TranslatableComponent(
					"multiplayer.player.joined",
					new Object[] {serverPlayer.getDisplayName()}
				);
			}
			else {
				mutableText2 = new TranslatableComponent(
					"multiplayer.player.joined.renamed",
					new Object[] {serverPlayer.getDisplayName(), string}
				);
			}
			
			this.broadcastMessage(mutableText2.withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
			serverPlayNetworkHandler.teleport(
				serverPlayer.getX(),
				serverPlayer.getY(),
				serverPlayer.getZ(),
				serverPlayer.getYRot(),
				serverPlayer.getXRot()
			);
			this.players.add(serverPlayer);
			this.playersByUUID.put(serverPlayer.getUUID(), serverPlayer);
			this.broadcastAll(new ClientboundPlayerInfoPacket(
				ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
				new ServerPlayer[] {serverPlayer}
			));
			
			for (ServerPlayer player : this.players) {
				serverPlayer.connection.send(new ClientboundPlayerInfoPacket(
					ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
					new ServerPlayer[] {(ServerPlayer) player}
				));
			}
			
			serverLevel3.addNewPlayer(serverPlayer);
			this.server.getCustomBossEvents().onPlayerConnect(serverPlayer);
			this.sendLevelInfo(serverPlayer, serverLevel3);
			if (!this.server.getResourcePack().isEmpty()) {
				serverPlayer.sendTexturePack(
					this.server.getResourcePack(),
					this.server.getResourcePackHash(),
					this.server.isResourcePackRequired(),
					this.server.getResourcePackPrompt()
				);
			}
			
			for (MobEffectInstance statusEffectInstance : serverPlayer.getActiveEffects()) {
				serverPlayNetworkHandler.send(new ClientboundUpdateMobEffectPacket(
					serverPlayer.getId(),
					statusEffectInstance
				));
			}
			
			if (compoundTag != null && compoundTag.contains("RootVehicle", 10)) {
				CompoundTag compoundTag2 = compoundTag.getCompound("RootVehicle");
				Entity entity = EntityType.loadEntityRecursive(
					compoundTag2.getCompound("Entity"),
					serverLevel3,
					(vehicle) -> {
						return !serverLevel3.addWithUUID(vehicle) ? null : vehicle;
					}
				);
				if (entity != null) {
					UUID uUID2;
					if (compoundTag2.hasUUID("Attach")) {
						uUID2 = compoundTag2.getUUID("Attach");
					}
					else {
						uUID2 = null;
					}
					
					Iterator<?> var21;
					Entity entity3;
					if (entity.getUUID().equals(uUID2)) {
						serverPlayer.startRiding(entity, true);
					}
					else {
						var21 = entity.getIndirectPassengers().iterator();
						
						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							if (entity3.getUUID().equals(uUID2)) {
								serverPlayer.startRiding(entity3, true);
								break;
							}
						}
					}
					
					if (!serverPlayer.isPassenger()) {
						LOGGER.warn("Couldn't reattach entity to player");
						entity.discard();
						var21 = entity.getIndirectPassengers().iterator();
						
						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							entity3.discard();
						}
					}
				}
			}
			
			serverPlayer.initInventoryMenu();
			info.cancel();
		}
	}
	
	@Shadow
	public CompoundTag load(ServerPlayer player) {
		return null;
	}
	
	// @Shadow
	// private void updatePlayerGameMode(ServerPlayer player, @Nullable ServerPlayer oldPlayer, ServerLevel world) {}
	
	@Shadow
	public void sendPlayerPermissionLevel(ServerPlayer player) {
	}
	
	@Shadow
	public int getPlayerCount() {
		return 0;
	}
	
	@Shadow
	public int getMaxPlayers() {
		return 0;
	}
	
	@Shadow
	public MinecraftServer getServer() {
		return null;
	}
	
	@Shadow
	protected void updateEntireScoreboard(ServerScoreboard scoreboard, ServerPlayer player) {
	}
	
	@Shadow
	public void broadcastMessage(Component message, ChatType type, UUID senderUuid) {
	}
	
	@Shadow
	public void broadcastAll(Packet<?> packet) {
	}
	
	@Shadow
	public void sendLevelInfo(ServerPlayer player, ServerLevel world) {
	}
}
