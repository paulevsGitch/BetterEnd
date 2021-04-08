package ru.betterend.util;

import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnRestriction.Location;
import net.minecraft.world.entity.SpawnRestriction.SpawnPredicate;
import net.minecraft.world.entity.mob.MobEntity;
import net.minecraft.world.Heightmap.Type;

public class SpawnHelper {
	public static <T extends MobEntity> void restrictionAir(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Location.NO_RESTRICTIONS, Type.MOTION_BLOCKING, predicate);
	}

	public static <T extends MobEntity> void restrictionLand(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Location.ON_GROUND, Type.MOTION_BLOCKING, predicate);
	}

	public static <T extends MobEntity> void restrictionWater(EntityType<T> entity, SpawnPredicate<T> predicate) {
		SpawnRestrictionAccessor.callRegister(entity, Location.IN_WATER, Type.MOTION_BLOCKING, predicate);
	}
}
