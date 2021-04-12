package ru.betterend.mixin.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
	@Final
	@Shadow
	private static Logger LOGGER;

	@Final
	@Shadow
	private MinecraftServer server;

	@Final
	@Shadow
	private RegistryAccess.RegistryHolder registryManager;

	@Shadow
	private int viewDistance;

	@Final
	@Shadow
	private List<ServerPlayer> players;

	@Final
	@Shadow
	private Map<UUID, ServerPlayer> playerMap;

	@Inject(method = "onPlayerConnect", at = @At(value = "HEAD"), cancellable = true)
	public void be_onPlayerConnect(Connection connection, ServerPlayer player, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			GameProfile gameProfile = player.getGameProfile();
			GameProfileCache userCache = this.server.getProfileCache();
			GameProfile gameProfile2 = userCache.get(gameProfile.getId());
			String string = gameProfile2 == null ? gameProfile.getName() : gameProfile2.getName();
			userCache.add(gameProfile);
			CompoundTag compoundTag = this.loadPlayerData(player);
			ResourceKey<Level> var23;
			if (compoundTag != null) {
				DataResult<ResourceKey<Level>> var10000 = DimensionType.parseLegacy(new Dynamic<Tag>(NbtOps.INSTANCE, compoundTag.get("Dimension")));
				Logger var10001 = LOGGER;
				var10001.getClass();
				var23 = (ResourceKey<Level>) var10000.resultOrPartial(var10001::error).orElse(Level.END);
			}
			else {
				var23 = Level.END;
			}

			ResourceKey<Level> registryKey = var23;
			ServerLevel serverWorld = this.server.getLevel(registryKey);
			ServerLevel serverWorld3;
			if (serverWorld == null) {
				LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", registryKey);
				serverWorld3 = this.server.overworld();
			}
			else {
				serverWorld3 = serverWorld;
			}

			player.setLevel(serverWorld3);
			player.gameMode.setLevel((ServerLevel) player.level);
			String string2 = "local";
			if (connection.getRemoteAddress() != null) {
				string2 = connection.getRemoteAddress().toString();
			}

			LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", player.getName().getString(), string2, player.getId(), player.getX(), player.getY(), player.getZ());
			LevelData worldProperties = serverWorld3.getLevelData();
			this.setGameMode(player, (ServerPlayer) null, serverWorld3);
			ServerGamePacketListenerImpl serverPlayNetworkHandler = new ServerGamePacketListenerImpl(this.server, connection, player);
			GameRules gameRules = serverWorld3.getGameRules();
			boolean bl = gameRules.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN);
			boolean bl2 = gameRules.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO);
			serverPlayNetworkHandler.send(new ClientboundLoginPacket(player.getId(), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), BiomeManager.obfuscateSeed(serverWorld3.getSeed()),
					worldProperties.isHardcore(), this.server.levelKeys(), this.registryManager, serverWorld3.dimensionType(), serverWorld3.dimension(), this.getMaxPlayerCount(), this.viewDistance, bl2, !bl,
					serverWorld3.isDebug(), serverWorld3.isFlat()));
			serverPlayNetworkHandler.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.BRAND, (new FriendlyByteBuf(Unpooled.buffer())).writeUtf(this.getServer().getServerModName())));
			serverPlayNetworkHandler.send(new ClientboundChangeDifficultyPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
			serverPlayNetworkHandler.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
			serverPlayNetworkHandler.send(new ClientboundSetCarriedItemPacket(player.inventory.selected));
			serverPlayNetworkHandler.send(new ClientboundUpdateRecipesPacket(this.server.getRecipeManager().getRecipes()));
			serverPlayNetworkHandler.send(new ClientboundUpdateTagsPacket(this.server.getTags()));
			this.sendCommandTree(player);
			player.getStats().markAllDirty();
			player.getRecipeBook().sendInitialRecipeBook(player);
			this.sendScoreboard(serverWorld3.getScoreboard(), player);
			this.server.invalidateStatus();
			TranslatableComponent mutableText2;
			if (player.getGameProfile().getName().equalsIgnoreCase(string)) {
				mutableText2 = new TranslatableComponent("multiplayer.player.joined", new Object[] { player.getDisplayName() });
			}
			else {
				mutableText2 = new TranslatableComponent("multiplayer.player.joined.renamed", new Object[] { player.getDisplayName(), string });
			}

			this.broadcastChatMessage(mutableText2.withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
			serverPlayNetworkHandler.teleport(player.getX(), player.getY(), player.getZ(), player.yRot, player.xRot);
			this.players.add(player);
			this.playerMap.put(player.getUUID(), player);
			this.sendToAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { player }));

			for (int i = 0; i < this.players.size(); ++i) {
				player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, new ServerPlayer[] { (ServerPlayer) this.players.get(i) }));
			}

			serverWorld3.addNewPlayer(player);
			this.server.getCustomBossEvents().onPlayerConnect(player);
			this.sendWorldInfo(player, serverWorld3);
			if (!this.server.getResourcePack().isEmpty()) {
				player.sendTexturePack(this.server.getResourcePack(), this.server.getResourcePackHash());
			}

			Iterator<?> var24 = player.getActiveEffects().iterator();

			while (var24.hasNext()) {
				MobEffectInstance statusEffectInstance = (MobEffectInstance) var24.next();
				serverPlayNetworkHandler.send(new ClientboundUpdateMobEffectPacket(player.getId(), statusEffectInstance));
			}

			if (compoundTag != null && compoundTag.contains("RootVehicle", 10)) {
				CompoundTag compoundTag2 = compoundTag.getCompound("RootVehicle");
				Entity entity = EntityType.loadEntityRecursive(compoundTag2.getCompound("Entity"), serverWorld3, (vehicle) -> {
					return !serverWorld3.addWithUUID(vehicle) ? null : vehicle;
				});
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
						player.startRiding(entity, true);
					}
					else {
						var21 = entity.getIndirectPassengers().iterator();

						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							if (entity3.getUUID().equals(uUID2)) {
								player.startRiding(entity3, true);
								break;
							}
						}
					}

					if (!player.isPassenger()) {
						LOGGER.warn("Couldn't reattach entity to player");
						serverWorld3.despawn(entity);
						var21 = entity.getIndirectPassengers().iterator();

						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							serverWorld3.despawn(entity3);
						}
					}
				}
			}

			player.initMenu();
			info.cancel();
		}
	}

	@Shadow
	public CompoundTag loadPlayerData(ServerPlayer player) {
		return null;
	}

	@Shadow
	private void setGameMode(ServerPlayer player, @Nullable ServerPlayer oldPlayer, ServerLevel world) {}

	@Shadow
	public void sendCommandTree(ServerPlayer player) {}

	@Shadow
	public int getMaxPlayerCount() {
		return 0;
	}

	@Shadow
	public MinecraftServer getServer() {
		return null;
	}

	@Shadow
	protected void sendScoreboard(ServerScoreboard scoreboard, ServerPlayer player) {}

	@Shadow
	public void broadcastChatMessage(Component message, ChatType type, UUID senderUuid) {}

	@Shadow
	public void sendToAll(Packet<?> packet) {}

	@Shadow
	public void sendWorldInfo(ServerPlayer player, ServerLevel world) {}
}
