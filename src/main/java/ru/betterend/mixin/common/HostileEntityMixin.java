package ru.betterend.mixin.common;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.mob.EndermanEntity;
import net.minecraft.world.entity.mob.HostileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
	@Inject(method = "canSpawnInDark", at = @At(value = "RETURN"), cancellable = true)
	private static void be_endermenCheck(EntityType<? extends HostileEntity> type,
			ServerLevelAccessor serverWorldAccess, SpawnReason spawnReason, BlockPos pos, Random random,
			CallbackInfoReturnable<Boolean> info) {
		boolean canSpawn = info.getReturnValue();
		if (canSpawn && spawnReason == SpawnReason.NATURAL && type == EntityType.ENDERMAN) {
			Box box = new Box(pos).expand(16);
			List<EndermanEntity> entities = serverWorldAccess.getEntitiesByClass(EndermanEntity.class, box,
					(entity) -> {
						return true;
					});
			info.setReturnValue(entities.size() < 6);
		}
	}
}
