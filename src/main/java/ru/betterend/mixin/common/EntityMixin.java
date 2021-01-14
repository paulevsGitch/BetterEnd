package ru.betterend.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

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
	private static final Map<Entity, BlockPos> EXIT_POS = Maps.newHashMap();
	
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
	protected abstract TeleportTarget getTeleportTarget(ServerWorld destination);
	
	@Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
	public void be_moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
		Entity entity = (Entity) (Object) this;
		if (!removed && beCanTeleport() && world instanceof ServerWorld) {
			this.detach();
			this.world.getProfiler().push("changeDimension");
			this.world.getProfiler().push("reposition");
			TeleportTarget teleportTarget = this.getTeleportTarget(destination);
			if (teleportTarget != null) {
				this.world.getProfiler().swap("reloading");
				entity = this.getType().create(destination);
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
				be_resetTeleport();
				info.setReturnValue(entity);
				info.cancel();
			}
		}
	}
	
	@Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
	protected void be_getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> info) {
		if (beCanTeleport()) {
			BlockPos pos = EXIT_POS.get(be_getSelf());
			info.setReturnValue(new TeleportTarget(new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), getVelocity(), yaw, pitch));
			be_resetTeleport();
			info.cancel();
		}
	}
	
	@Shadow
	protected void setRotation(float yaw, float pitch) {}

	@Override
	public void beSetExitPos(BlockPos pos) {
		EXIT_POS.put(be_getSelf(), pos.toImmutable());
	}
	
	private void be_resetTeleport() {
		EXIT_POS.remove(be_getSelf());
	}
	
	@Override
	public boolean beCanTeleport() {
		return EXIT_POS.containsKey(be_getSelf());
	}
	
	private Entity be_getSelf() {
		return (Entity) (Object) this;
	}
}
