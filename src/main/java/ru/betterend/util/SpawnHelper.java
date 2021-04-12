package ru.betterend.util;

import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements.SpawnPredicate;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class SpawnHelper {
	public static <T extends Mob> void restrictionAir(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Type.NO_RESTRICTIONS, Types.MOTION_BLOCKING, predicate);
	}
	
	public static <T extends Mob> void restrictionLand(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Type.ON_GROUND, Types.MOTION_BLOCKING, predicate);
	}
	
	public static <T extends Mob> void restrictionWater(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Type.IN_WATER, Types.MOTION_BLOCKING, predicate);
	}
}
