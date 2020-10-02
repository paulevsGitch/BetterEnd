package ru.betterend.mixin.common;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(FDD)V", ordinal = 0))
	private float increaseKnockback(float value, double x, double z) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		value += this.getKnockback(player.getMainHandStack().getItem());
		return value;
	}
	
	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V", shift = Shift.BEFORE))
	public void attack(Entity target, CallbackInfo cinfo) {
		if (target instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) (Object) this;
			int baseKnockback = EnchantmentHelper.getKnockback(player);
			if (isSprinting() && getAttackCooldownProgress(0.5F) > 0.9F) {
				baseKnockback++;
			}
			if (baseKnockback == 0) {
				Item tool = player.getMainHandStack().getItem();
				LivingEntity livingTarget = (LivingEntity) target;
				livingTarget.takeKnockback((float) this.getKnockback(tool), MathHelper.sin(yaw * 0.017453292F), -MathHelper.cos(yaw * 0.017453292F));
			}
		}
	}
	
	@Shadow
	abstract float getAttackCooldownProgress(float base);
	
	private double getKnockback(Item tool) {
		if (tool == null) return 0.0D;
		Collection<EntityAttributeModifier> modifiers = tool.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
		if (modifiers.size() > 0) {
			return modifiers.iterator().next().getValue();
		}
		return 0.0D;
	}
}
