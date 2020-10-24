package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.betterend.entity.EntityEndSlime;
import ru.betterend.util.BlocksHelper;

@Mixin(targets = "net.minecraft.entity.mob.SlimeEntity$MoveGoal")
public class SlimeEntityMoveGoalMixin {
	@Shadow
	@Final
	private SlimeEntity slime;

	@Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
	private void canStart(CallbackInfoReturnable<Boolean> info) {
		if (!slime.hasVehicle() && slime instanceof EntityEndSlime) {
			float yaw = slime.getHeadYaw();
			float speed = slime.getMovementSpeed();
			if (speed > 0.1) {
				Vec3d dir = Vec3d.fromPolar(0, yaw);
				BlockPos pos = slime.getBlockPos().add(dir.getX() * speed * 4, 0, dir.getZ() * speed * 4);
				int down = BlocksHelper.downRay(slime.getEntityWorld(), pos, 16);
				info.setReturnValue(down < 5);
				info.cancel();
			}
		}
	}
}
