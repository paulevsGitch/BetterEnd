package ru.betterend.mixin.common;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
	@Inject(method = "canSpawnInDark", at = @At(value = "RETURN"), cancellable = true)
	private static void endermenCheck(EntityType<? extends HostileEntity> type, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> info) {
		boolean canSpawn = info.getReturnValue();
		if (canSpawn && spawnReason == SpawnReason.NATURAL && type == EntityType.ENDERMAN) {
			Box box = new Box(pos).expand(16);
			List<EndermanEntity> entities = serverWorldAccess.getEntitiesByClass(EndermanEntity.class, box, (entity) -> { return true; });
			info.setReturnValue(entities.size() < 6);
		}
	}
}
