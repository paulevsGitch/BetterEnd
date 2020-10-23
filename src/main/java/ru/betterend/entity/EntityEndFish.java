package ru.betterend.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class EntityEndFish extends SchoolingFishEntity {
	public static final int VARIANTS = 5;
	private static final TrackedData<Byte> VARIANT = DataTracker.registerData(EntityEndFish.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Byte> SCALE = DataTracker.registerData(EntityEndFish.class, TrackedDataHandlerRegistry.BYTE);
	
	public EntityEndFish(EntityType<EntityEndFish> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, (byte) this.getRandom().nextInt(VARIANTS));
		this.dataTracker.startTracking(SCALE, (byte) this.getRandom().nextInt(255));
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
	
	public int getVariant() {
		return (int) this.dataTracker.get(VARIANT);
	}
	
	public float getScale() {
		return ((int) this.dataTracker.get(SCALE) & 255) / 512F + 0.6F;
	}
}
