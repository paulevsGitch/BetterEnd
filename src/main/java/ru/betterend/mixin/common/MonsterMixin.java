package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(Monster.class)
public class MonsterMixin {
	@Inject(method = "checkMonsterSpawnRules", at = @At(value = "RETURN"), cancellable = true)
	private static void be_checkMonsterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> info) {
		boolean canSpawn = info.getReturnValue();
		if (canSpawn && spawnReason == MobSpawnType.NATURAL && type == EntityType.ENDERMAN) {
			AABB box = new AABB(pos).inflate(16);
			List<EnderMan> entities = serverWorldAccess.getEntitiesOfClass(EnderMan.class, box, (entity) -> {
				return true;
			});
			info.setReturnValue(entities.size() < 6);
		}
	}
}
