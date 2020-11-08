package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import ru.betterend.effects.EndEnchantments;
import ru.betterend.effects.EndStatusEffects;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin {
	
	@Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
	private void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (player.isCreative() || player.hasStatusEffect(EndStatusEffects.END_VEIL) ||
				EnchantmentHelper.getLevel(EndEnchantments.END_VEIL, player.getEquippedStack(EquipmentSlot.HEAD)) > 0) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
}
