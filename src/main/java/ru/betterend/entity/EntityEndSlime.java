package ru.betterend.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import ru.betterend.registry.BiomeRegistry;

public class EntityEndSlime extends SlimeEntity {
	private static final TrackedData<Boolean> MOSSY = DataTracker.registerData(EntityEndSlime.class, TrackedDataHandlerRegistry.BOOLEAN);
	
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
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (world.getRandom().nextBoolean() && BiomeRegistry.getFromBiome(world.getBiome(getBlockPos())) == BiomeRegistry.FOGGY_MUSHROOMLAND) {
			this.setMossy(true);
		}
		return data;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(MOSSY, false);
	}

	@Override
	protected ParticleEffect getParticles() {
		return ParticleTypes.PORTAL;
	}
	
	protected void setMossy(boolean mossy) {
		this.dataTracker.set(MOSSY, mossy);
	}

	public boolean isMossy() {
		return this.dataTracker.get(MOSSY);
	}
}
