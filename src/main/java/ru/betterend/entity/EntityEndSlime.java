package ru.betterend.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class EntityEndSlime extends SlimeEntity {
	public EntityEndSlime(EntityType<EntityEndSlime> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D);
	}

	@Override
	protected ParticleEffect getParticles() {
		return ParticleTypes.PORTAL;
	}
}
