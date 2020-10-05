package ru.betterend.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.SpawnRestriction.Location;
import net.minecraft.entity.SpawnRestriction.SpawnPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap.Type;
import ru.betterend.BetterEnd;

public class SpawnHelper {
	private static Method regRestriction;
	
	public static <T extends MobEntity> void restriction(EntityType<T> entity, Location location, Type heughtmapType, SpawnPredicate<T> predicate) {
		try {
			regRestriction.invoke(null, entity, location, heughtmapType, predicate);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			BetterEnd.LOGGER.error(e.getMessage());
		}
	}
	
	public static <T extends MobEntity> void restrictionLand(EntityType<T> entity, SpawnPredicate<T> predicate) {
		restriction(entity, Location.ON_GROUND, Type.MOTION_BLOCKING, predicate);
	}
	
	static {
		try {
			regRestriction = SpawnRestriction.class.getDeclaredMethod("register", EntityType.class, Location.class, Type.class, SpawnPredicate.class);
			regRestriction.setAccessible(true);
		} 
		catch (NoSuchMethodException | SecurityException e) {
			BetterEnd.LOGGER.error(e.getMessage());
		}
	}
}
