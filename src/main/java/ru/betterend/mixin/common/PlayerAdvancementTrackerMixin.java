package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;

import ru.betterend.events.PlayerAdvancementsEvents;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
	
	@Shadow
	private ServerPlayerEntity owner;
	
	@Inject(method = "grantCriterion", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			shift = Shift.AFTER))
	public void be_onAdvancementComplete(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> info) {
		PlayerAdvancementsEvents.PLAYER_ADVENCEMENT_COMPLETE.invoker().onAdvancementComplete(owner, advancement, criterionName);
	}
}
