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

public class EntityRegistry {
	public static final EntityType<EntityDragonfly> DRAGONFLY = register("dragonfly", SpawnGroup.CREATURE, 0.6F, 0.5F, EntityDragonfly::new, EntityDragonfly.createMobAttributes());
	
	public static void register() {}
	
	protected static <T extends Entity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build());
		return type;
	}
	
	private static <T extends LivingEntity> EntityType<T> register(String name, SpawnGroup group, float width, float height, EntityFactory<T> entity, Builder attributes) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, BetterEnd.makeID(name), FabricEntityTypeBuilder.<T>create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build());
		FabricDefaultAttributeRegistry.register(type, attributes);
		return type;
	}
}
