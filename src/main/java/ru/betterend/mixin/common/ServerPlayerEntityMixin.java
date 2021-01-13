package ru.betterend.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import ru.betterend.interfaces.TeleportingEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements TeleportingEntity {
	private static final Map<ServerPlayerEntity, Long> COOLDOWN = Maps.newHashMap();
	
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	@Shadow
	private RegistryKey<World> spawnPointDimension;
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void be_baseTick(CallbackInfo info) {
		if (hasCooldown()) {
			ServerPlayerEntity key = (ServerPlayerEntity) (Object) this;
			long value = COOLDOWN.getOrDefault(key, 0L) - 1;
			COOLDOWN.put(key, value);
		}
	}
	
	@Shadow
	private void moveToSpawn(ServerWorld world) {}
	
	@Override
	public long beGetCooldown() {
		ServerPlayerEntity key = (ServerPlayerEntity) (Object) this;
		return COOLDOWN.getOrDefault(key, 0L);
	}

	@Override
	public void beSetCooldown(long time) {
		ServerPlayerEntity key = (ServerPlayerEntity) (Object) this;
		COOLDOWN.put(key, time);
	}

	@Override
	public void beSetExitPos(BlockPos pos) {}

	@Override
	public BlockPos beGetExitPos() {
		return null;
	}
}
