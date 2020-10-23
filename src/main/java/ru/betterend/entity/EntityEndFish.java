package ru.betterend.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class EntityEndFish extends SchoolingFishEntity {
	public EntityEndFish(EntityType<EntityEndFish> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected ItemStack getFishBucketItem() {
		return null;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return null;
	}
	
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D);
	}
}
