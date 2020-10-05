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
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityDragonfly;
import ru.betterend.entity.EntityEndSlime;
import ru.betterend.util.MHelper;

public class EntityRegistry {
	public static final EntityType<EntityDragonfly> DRAGONFLY = register("dragonfly", SpawnGroup.AMBIENT, 0.6F, 0.5F, EntityDragonfly::new, EntityDragonfly.createMobAttributes(), MHelper.color(32, 42, 176), MHelper.color(115, 225, 249));
	public static final EntityType<EntityEndSlime> END_SLIME = register("end_slime", SpawnGroup.AMBIENT, 0.6F, 0.5F, EntityEndSlime::new, EntityEndSlime.createMobAttributes(), MHelper.color(28, 28, 28), MHelper.color(99, 11, 99));
	
	public static void register() {}
	
	protected static <T extends Entity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build());
		return type;
	}
	
	private static <T extends LivingEntity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity, Builder attributes, int eggColor, int dotsColor) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build());
		FabricDefaultAttributeRegistry.register(type, attributes);
		ItemRegistry.registerEgg("spawn_egg_" + name, type, eggColor, dotsColor);
		return type;
	}
}
