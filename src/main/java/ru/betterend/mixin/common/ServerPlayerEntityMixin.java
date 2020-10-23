package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import ru.betterend.interfaces.TeleportingEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements TeleportingEntity {
	
	private long beCooldown;
	
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
	public void beSetExitPos(BlockPos pos) {}

	@Override
	public BlockPos beGetExitPos() {
		return null;
	}
}
