package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import ru.betterend.interfaces.TeleportingEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements TeleportingEntity {
	
	private BlockPos beExitPos;
	private long beCooldown;
	
	@Shadow
	public float yaw;
	@Shadow
	public float pitch;
	@Shadow
	public boolean removed;
	@Shadow
	public World world;
	
	@Final
	@Shadow
	public abstract void detach();
	
	@Shadow
	public abstract Vec3d getVelocity();
	
	@Shadow
	public abstract EntityType<?> getType();
	
	@Shadow
	public abstract void copyFrom(Entity original);
	
	@Shadow
	public abstract Entity moveToWorld(ServerWorld destination);
	
	@Shadow
	protected abstract TeleportTarget getTeleportTarget(ServerWorld destination);
	
	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
		if (!removed && beExitPos != null && world instanceof ServerWorld) {
			this.detach();
			this.world.getProfiler().push("changeDimension");
			this.world.getProfiler().push("reposition");
			TeleportTarget teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				this.world.getProfiler().swap("reloading");
				Entity entity = this.getType().create(destination);
				if (entity != null) {
					entity.copyFrom(Entity.class.cast(this));
					entity.refreshPositionAndAngles(teleportTarget.position.x, teleportTarget.position.y, teleportTarget.position.z, teleportTarget.yaw, entity.pitch);
					entity.setVelocity(teleportTarget.velocity);
					destination.onDimensionChanged(entity);
				}
				this.removed = true;
				this.world.getProfiler().pop();
				((ServerWorld) world).resetIdleTimeout();
				destination.resetIdleTimeout();
				this.world.getProfiler().pop();
				this.beExitPos = null;
				ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.invoker().afterChangeWorld(Entity.class.cast(this), entity, (ServerWorld) world, (ServerWorld) entity.world);
				info.setReturnValue(entity);
				info.cancel();
			}
		}
	}
	
	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beExitPos != null) {
			info.setReturnValue(new TeleportTarget(new Vec3d(beExitPos.getX() + 0.5D, beExitPos.getY(), beExitPos.getZ() + 0.5D), getVelocity(), yaw, pitch));
			info.cancel();
		}
	}
	
	@Inject(method = "baseTick", at = @At("TAIL"))
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
	public void beSetExitPos(BlockPos pos) {
		this.beExitPos = pos;
	}

	@Override
	public BlockPos beGetExitPos() {
		return this.beExitPos;
	}
}
