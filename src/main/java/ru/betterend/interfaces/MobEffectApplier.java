package ru.betterend.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface MobEffectApplier {
	void applyEffect(LivingEntity owner);
}
