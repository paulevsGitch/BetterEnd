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
	private float yRot;
	@Shadow
	private float xRot;
	
	@Shadow
	public Level level;
	
	@Final
	@Shadow
	public abstract void unRide();
	
	@Shadow
	public abstract Vec3 getDeltaMovement();
	
	@Shadow
	public abstract EntityType<?> getType();
	
	@Shadow
	protected abstract PortalInfo findDimensionEntryPoint(ServerLevel destination);
	
	@Shadow
	protected abstract void removeAfterChangingDimensions();
	
	@Shadow
	public abstract boolean isRemoved();
	
	private BlockPos exitPos;
	
	@Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
	public void be_changeDimension(ServerLevel destination, CallbackInfoReturnable<Entity> info) {
		if (!isRemoved() && be_canTeleport() && level instanceof ServerLevel) {
			unRide();
			level.getProfiler().push("changeDimension");
			level.getProfiler().push("reposition");
			PortalInfo teleportTarget = findDimensionEntryPoint(destination);
			if (teleportTarget != null) {
				level.getProfiler().popPush("reloading");
				Entity entity = getType().create(destination);
				if (entity != null) {
					entity.restoreFrom(Entity.class.cast(this));
					entity.moveTo(
						teleportTarget.pos.x,
						teleportTarget.pos.y,
						teleportTarget.pos.z,
						teleportTarget.yRot,
						entity.getXRot()
					);
					entity.setDeltaMovement(teleportTarget.speed);
					//TODO: check if this works as intended in 1.17
					
					destination.addDuringTeleport(entity);
				}
				
				this.removeAfterChangingDimensions();
				level.getProfiler().pop();
				((ServerLevel) level).resetEmptyTime();
				destination.resetEmptyTime();
				level.getProfiler().pop();
				be_resetExitPos();
				info.setReturnValue(entity);
			}
		}
	}
	
	@Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
	protected void be_findDimensionEntryPoint(ServerLevel destination, CallbackInfoReturnable<PortalInfo> info) {
		if (be_canTeleport()) {
			info.setReturnValue(new PortalInfo(
				new Vec3(exitPos.getX() + 0.5, exitPos.getY(), exitPos.getZ() + 0.5),
				getDeltaMovement(),
				yRot,
				xRot
			));
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
