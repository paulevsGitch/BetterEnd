package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.interfaces.TeleportingEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements TeleportingEntity {
	@Shadow
	public float yRot;
	@Shadow
	public float xRot;
	@Shadow
	public boolean removed;
	@Shadow
	public Level world;
	
	@Final
	@Shadow
	public abstract void detach();
	
	@Shadow
	public abstract Vec3 getVelocity();
	
	@Shadow
	public abstract EntityType<?> getType();
	
	@Shadow
	protected abstract PortalInfo getTeleportTarget(ServerLevel destination);

	private BlockPos exitPos;

	@Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
	public void be_changeDimension(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (!removed && be_canTeleport() && world instanceof ServerLevel) {
			this.detach();
			this.world.getProfiler().push("changeDimension");
			this.world.getProfiler().push("reposition");
			PortalInfo teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				this.world.getProfiler().popPush("reloading");
				Entity entity = this.getType().create(destination);
				if (entity != null) {
					entity.restoreFrom(Entity.class.cast(this));
					entity.moveTo(teleportTarget.pos.x, teleportTarget.pos.y, teleportTarget.pos.z, teleportTarget.yRot, entity.xRot);
					entity.setDeltaMovement(teleportTarget.speed);
					destination.addFromAnotherDimension(entity);
				}
				this.removed = true;
				this.world.getProfiler().pop();
				((ServerLevel) world).resetEmptyTime();
				destination.resetEmptyTime();
				this.world.getProfiler().pop();
				this.be_resetExitPos();
				info.setReturnValue(entity);
			}
		}
	}
	
	@Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
	protected void be_findDimensionEntryPoint(ServerLevel destination, CallbackInfoReturnable<PortalInfo> info) {
		if (be_canTeleport()) {
			info.setReturnValue(new PortalInfo(new Vec3(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5), getVelocity(), yRot, xRot));
		}
	}

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
