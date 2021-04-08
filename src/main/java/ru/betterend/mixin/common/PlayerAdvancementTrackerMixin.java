package ru.betterend.mixin.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerPlayer;
import ru.betterend.events.PlayerAdvancementsEvents;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementTrackerMixin {

	@Shadow
	private ServerPlayer owner;

	@Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V", shift = Shift.AFTER))
	public void be_onAdvancementComplete(Advancement advancement, String criterionName,
	                                     CallbackInfoReturnable<Boolean> info) {
		PlayerAdvancementsEvents.PLAYER_ADVANCEMENT_COMPLETE.invoker().onAdvancementComplete(owner, advancement,
				criterionName);
	}
}
