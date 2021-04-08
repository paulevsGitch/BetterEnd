package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.level.Level;
import ru.betterend.interfaces.TeleportingEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements TeleportingEntity {

	@Shadow
	public float yaw;
	@Shadow
	public float pitch;
	@Shadow
	public boolean removed;
	@Shadow
	public Level world;

	@Final
	@Shadow
	public abstract void detach();

	@Shadow
	public abstract Vec3d getVelocity();

	@Shadow
	public abstract EntityType<?> getType();

	@Shadow
	protected abstract TeleportTarget getTeleportTarget(ServerLevel destination);

	private BlockPos exitPos;

	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void be_moveToWorld(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (!removed && beCanTeleport() && world instanceof ServerLevel) {
			this.detach();
			this.world.getProfiler().push("changeDimension");
			this.world.getProfiler().push("reposition");
			TeleportTarget teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				this.world.getProfiler().swap("reloading");
				Entity entity = this.getType().create(destination);
				if (entity != null) {
					entity.copyFrom(Entity.class.cast(this));
					entity.refreshPositionAndAngles(teleportTarget.position.x, teleportTarget.position.y,
							teleportTarget.position.z, teleportTarget.yaw, entity.pitch);
					entity.setVelocity(teleportTarget.velocity);
					destination.onDimensionChanged(entity);
				}
				this.removed = true;
				this.world.getProfiler().pop();
				((ServerLevel) world).resetIdleTimeout();
				destination.resetIdleTimeout();
				this.world.getProfiler().pop();
				this.beResetExitPos();
				info.setReturnValue(entity);
			}
		}
	}

	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void be_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beCanTeleport()) {
			info.setReturnValue(new TeleportTarget(
					new Vec3d(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getVelocity(), yaw, pitch));
		}
	}

	@Override
	public void beSetExitPos(BlockPos pos) {
		this.exitPos = pos.toImmutable();
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
