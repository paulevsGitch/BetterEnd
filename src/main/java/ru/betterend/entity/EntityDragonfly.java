package ru.betterend.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.world.World;

public class EntityDragonfly extends FlyingEntity {
    public EntityDragonfly(EntityType<EntityDragonfly> entityType, World world) {
        super(entityType, world);
    }
    
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
	}
}
