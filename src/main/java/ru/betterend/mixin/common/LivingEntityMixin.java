package ru.betterend.mixin.common;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.attribute.EntityAttributeModifier;
import net.minecraft.world.entity.attribute.EntityAttributes;
import net.minecraft.world.entity.damage.DamageSource;
import net.minecraft.world.item.Item;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	private Entity lastAttacker;

	@Inject(method = "damage", at = @At("HEAD"))
	public void be_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		this.lastAttacker = source.getAttacker();
	}

	@ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(FDD)V"))
	private float be_increaseKnockback(float value, double x, double z) {
		if (lastAttacker != null && lastAttacker instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) lastAttacker;
			value += this.be_getKnockback(attacker.getMainHandStack().getItem());
		}
		return value;
	}

	private double be_getKnockback(Item tool) {
		if (tool == null)
			return 0.0D;
		Collection<EntityAttributeModifier> modifiers = tool.getAttributeModifiers(EquipmentSlot.MAINHAND)
				.get(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
		if (modifiers.size() > 0) {
			return modifiers.iterator().next().getValue();
		}
		return 0.0D;
	}
}
