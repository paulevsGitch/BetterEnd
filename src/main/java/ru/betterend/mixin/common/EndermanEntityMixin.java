package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.mob.EndermanEntity;
import net.minecraft.world.entity.player.Player;
import ru.betterend.effects.EndEnchantments;
import ru.betterend.effects.EndMobEffects;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin {

	@Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
	private void be_isPlayerStaring(Player player, CallbackInfoReturnable<Boolean> info) {
		if (player.isCreative() || player.hasMobEffect(EndMobEffects.END_VEIL) || EnchantmentHelper
				.getLevel(EndEnchantments.END_VEIL, player.getEquippedStack(EquipmentSlot.HEAD)) > 0) {
			info.setReturnValue(false);
		}
	}
}
