package ru.betterend.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import ru.betterend.registry.EndEntities;

public class SilkMothEntity extends AnimalEntity implements Flutterer {
	public SilkMothEntity(EntityType<? extends SilkMothEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected boolean hasWings() {
		return true;
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean canClimb() {
		return false;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return EndEntities.SILK_MOTH.create(world);
	}
}
