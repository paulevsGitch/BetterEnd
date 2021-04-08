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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.HeldItemChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.level.dimension.DimensionType;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Final
	@Shadow
	private static Logger LOGGER;

	@Final
	@Shadow
	private MinecraftServer server;

	@Final
	@Shadow
	private DynamicRegistryManager.Impl registryManager;

	@Shadow
	private int viewDistance;

	@Final
	@Shadow
	private List<ServerPlayer> players;

	@Final
	@Shadow
	private Map<UUID, ServerPlayer> playerMap;

	@Inject(method = "onPlayerConnect", at = @At(value = "HEAD"), cancellable = true)
	public void be_onPlayerConnect(ClientConnection connection, ServerPlayer player, CallbackInfo info) {
		if (GeneratorOptions.swapOverworldToEnd()) {
			GameProfile gameProfile = player.getGameProfile();
			UserCache userCache = this.server.getUserCache();
			GameProfile gameProfile2 = userCache.getByUuid(gameProfile.getId());
			String string = gameProfile2 == null ? gameProfile.getName() : gameProfile2.getName();
			userCache.add(gameProfile);
			CompoundTag compoundTag = this.loadPlayerData(player);
			RegistryKey<Level> var23;
			if (compoundTag != null) {
				DataResult<RegistryKey<Level>> var10000 = DimensionType
						.method_28521(new Dynamic<Tag>(NbtOps.INSTANCE, compoundTag.get("Dimension")));
				Logger var10001 = LOGGER;
				var10001.getClass();
				var23 = (RegistryKey<Level>) var10000.resultOrPartial(var10001::error).orElse(Level.END);
			} else {
				var23 = Level.END;
			}

			RegistryKey<Level> registryKey = var23;
			ServerLevel serverWorld = this.server.getLevel(registryKey);
			ServerLevel serverWorld3;
			if (serverWorld == null) {
				LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", registryKey);
				serverWorld3 = this.server.getOverworld();
			} else {
				serverWorld3 = serverWorld;
			}

			player.setWorld(serverWorld3);
			player.interactionManager.setWorld((ServerLevel) player.world);
			String string2 = "local";
			if (connection.getAddress() != null) {
				string2 = connection.getAddress().toString();
			}

			LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", player.getName().getString(), string2,
					player.getEntityId(), player.getX(), player.getY(), player.getZ());
			WorldProperties worldProperties = serverWorld3.getLevelProperties();
			this.setGameMode(player, (ServerPlayer) null, serverWorld3);
			ServerPlayNetworkHandler serverPlayNetworkHandler = new ServerPlayNetworkHandler(this.server, connection,
					player);
			GameRules gameRules = serverWorld3.getGameRules();
			boolean bl = gameRules.getBoolean(GameRules.DO_IMMEDIATE_RESPAWN);
			boolean bl2 = gameRules.getBoolean(GameRules.REDUCED_DEBUG_INFO);
			serverPlayNetworkHandler.sendPacket(new GameJoinS2CPacket(player.getEntityId(),
					player.interactionManager.getGameMode(), player.interactionManager.getPreviousGameMode(),
					BiomeAccess.hashSeed(serverWorld3.getSeed()), worldProperties.isHardcore(),
					this.server.getWorldRegistryKeys(), this.registryManager, serverWorld3.getDimension(),
					serverWorld3.dimension(), this.getMaxPlayerCount(), this.viewDistance, bl2, !bl,
					serverWorld3.isDebugWorld(), serverWorld3.isFlat()));
			serverPlayNetworkHandler.sendPacket(new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND,
					(new PacketByteBuf(Unpooled.buffer())).writeString(this.getServer().getServerModName())));
			serverPlayNetworkHandler.sendPacket(
					new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
			serverPlayNetworkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.abilities));
			serverPlayNetworkHandler.sendPacket(new HeldItemChangeS2CPacket(player.inventory.selectedSlot));
			serverPlayNetworkHandler
					.sendPacket(new SynchronizeRecipesS2CPacket(this.server.getRecipeManager().values()));
			serverPlayNetworkHandler.sendPacket(new SynchronizeTagsS2CPacket(this.server.getTagManager()));
			this.sendCommandTree(player);
			player.getStatHandler().updateStatSet();
			player.getRecipeBook().sendInitRecipesPacket(player);
			this.sendScoreboard(serverWorld3.getScoreboard(), player);
			this.server.forcePlayerSampleUpdate();
			TranslatableText mutableText2;
			if (player.getGameProfile().getName().equalsIgnoreCase(string)) {
				mutableText2 = new TranslatableText("multiplayer.player.joined",
						new Object[] { player.getDisplayName() });
			} else {
				mutableText2 = new TranslatableText("multiplayer.player.joined.renamed",
						new Object[] { player.getDisplayName(), string });
			}

			this.broadcastChatMessage(mutableText2.formatted(Formatting.YELLOW), MessageType.SYSTEM, Util.NIL_UUID);
			serverPlayNetworkHandler.requestTeleport(player.getX(), player.getY(), player.getZ(), player.yaw,
					player.pitch);
			this.players.add(player);
			this.playerMap.put(player.getUuid(), player);
			this.sendToAll(
					new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, new ServerPlayer[] { player }));

			for (int i = 0; i < this.players.size(); ++i) {
				player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER,
						new ServerPlayer[] { (ServerPlayer) this.players.get(i) }));
			}

			serverWorld3.onPlayerConnected(player);
			this.server.getBossBarManager().onPlayerConnect(player);
			this.sendWorldInfo(player, serverWorld3);
			if (!this.server.getResourcePackUrl().isEmpty()) {
				player.sendResourcePackUrl(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
			}

			Iterator<?> var24 = player.getStatusEffects().iterator();

			while (var24.hasNext()) {
				StatusEffectInstance statusEffectInstance = (StatusEffectInstance) var24.next();
				serverPlayNetworkHandler
						.sendPacket(new EntityStatusEffectS2CPacket(player.getEntityId(), statusEffectInstance));
			}

			if (compoundTag != null && compoundTag.contains("RootVehicle", 10)) {
				CompoundTag compoundTag2 = compoundTag.getCompound("RootVehicle");
				Entity entity = EntityType.loadEntityWithPassengers(compoundTag2.getCompound("Entity"), serverWorld3,
						(vehicle) -> {
							return !serverWorld3.tryLoadEntity(vehicle) ? null : vehicle;
						});
				if (entity != null) {
					UUID uUID2;
					if (compoundTag2.containsUuid("Attach")) {
						uUID2 = compoundTag2.getUuid("Attach");
					} else {
						uUID2 = null;
					}

					Iterator<?> var21;
					Entity entity3;
					if (entity.getUuid().equals(uUID2)) {
						player.startRiding(entity, true);
					} else {
						var21 = entity.getPassengersDeep().iterator();

						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							if (entity3.getUuid().equals(uUID2)) {
								player.startRiding(entity3, true);
								break;
							}
						}
					}

					if (!player.hasVehicle()) {
						LOGGER.warn("Couldn't reattach entity to player");
						serverWorld3.removeEntity(entity);
						var21 = entity.getPassengersDeep().iterator();

						while (var21.hasNext()) {
							entity3 = (Entity) var21.next();
							serverWorld3.removeEntity(entity3);
						}
					}
				}
			}

			player.onSpawn();
			info.cancel();
		}
	}

	@Shadow
	public CompoundTag loadPlayerData(ServerPlayer player) {
		return null;
	}

	@Shadow
	private void setGameMode(ServerPlayer player, @Nullable ServerPlayer oldPlayer, ServerLevel world) {
	}

	@Shadow
	public void sendCommandTree(ServerPlayer player) {
	}

	@Shadow
	public int getMaxPlayerCount() {
		return 0;
	}

	@Shadow
	public MinecraftServer getServer() {
		return null;
	}

	@Shadow
	protected void sendScoreboard(ServerScoreboard scoreboard, ServerPlayer player) {
	}

	@Shadow
	public void broadcastChatMessage(Text message, MessageType type, UUID senderUuid) {
	}

	@Shadow
	public void sendToAll(Packet<?> packet) {
	}

	@Shadow
	public void sendWorldInfo(ServerPlayer player, ServerLevel world) {
	}
}
