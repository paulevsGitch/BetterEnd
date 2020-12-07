package ru.betterend.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityDragonfly;
import ru.betterend.entity.EntityEndFish;
import ru.betterend.entity.EntityEndSlime;
import ru.betterend.entity.EntityCubozoa;
import ru.betterend.entity.EntityShadowWalker;
import ru.betterend.util.MHelper;
import ru.betterend.util.SpawnHelper;

public class EndEntities {
	public static final EntityType<EntityDragonfly> DRAGONFLY = register("dragonfly", SpawnGroup.AMBIENT, 0.6F, 0.5F, EntityDragonfly::new, EntityDragonfly.createMobAttributes(), true, MHelper.color(32, 42, 176), MHelper.color(115, 225, 249));
	public static final EntityType<EntityEndSlime> END_SLIME = register("end_slime", SpawnGroup.MONSTER, 2F, 2F, EntityEndSlime::new, EntityEndSlime.createMobAttributes(), false, MHelper.color(28, 28, 28), MHelper.color(99, 11, 99));
	public static final EntityType<EntityEndFish> END_FISH = register("end_fish", SpawnGroup.WATER_AMBIENT, 0.5F, 0.5F, EntityEndFish::new, EntityEndFish.createMobAttributes(), true, MHelper.color(3, 50, 76), MHelper.color(120, 206, 255));
	public static final EntityType<EntityShadowWalker> SHADOW_WALKER = register("shadow_walker", SpawnGroup.MONSTER, 0.6F, 1.95F, EntityShadowWalker::new, EntityShadowWalker.createMobAttributes(), true, MHelper.color(30, 30, 30), MHelper.color(5, 5, 5));
	public static final EntityType<EntityCubozoa> CUBOZOA = register("cubozoa", SpawnGroup.WATER_AMBIENT, 0.6F, 1F, EntityCubozoa::new, EntityCubozoa.createMobAttributes(), true, MHelper.color(151, 77, 181), MHelper.color(93, 176, 238));
	
	public static void register() {
		SpawnHelper.restrictionLand(END_SLIME, EntityEndSlime::canSpawn);
		SpawnHelper.restrictionWater(END_FISH, EntityEndFish::canSpawn);
		SpawnHelper.restrictionLand(SHADOW_WALKER, HostileEntity::canSpawnInDark);
		SpawnHelper.restrictionWater(CUBOZOA, EntityCubozoa::canSpawn);
	}
	
	protected static <T extends Entity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build());
		return type;
	}
	
	private static <T extends LivingEntity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity, Builder attributes, boolean fixedSize, int eggColor, int dotsColor) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(fixedSize ? EntityDimensions.fixed(width, height) : EntityDimensions.changing(width, height)).build());
		FabricDefaultAttributeRegistry.register(type, attributes);
		EndItems.registerEgg("spawn_egg_" + name, type, eggColor, dotsColor);
		return type;
	}
}
