package ru.betterend.mixin.common;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	private Entity lastAttacker;
	
	@Inject(method = "hurt", at = @At("HEAD"))
	public void be_hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		this.lastAttacker = source.getEntity();
	}
	
	@ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(FDD)V"))
	private float be_increaseKnockback(float value, double x, double z) {
		if (lastAttacker != null && lastAttacker instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) lastAttacker;
			value += this.be_getKnockback(attacker.getMainHandItem().getItem());
		}
		return value;
	}
	
	private double be_getKnockback(Item tool) {
		if (tool == null) return 0.0D;
		Collection<AttributeModifier> modifiers = tool.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_KNOCKBACK);
		if (modifiers.size() > 0) {
			return modifiers.iterator().next().getAmount();
		}
		return 0.0D;
	}
}
