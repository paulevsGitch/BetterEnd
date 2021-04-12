package ru.betterend.mixin.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.events.PlayerAdvancementsEvents;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementTrackerMixin {
	
	@Shadow
	private ServerPlayer owner;
	
	@Inject(method = "grantCriterion", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			shift = Shift.AFTER))
	public void be_onAdvancementComplete(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> info) {
		PlayerAdvancementsEvents.PLAYER_ADVENCEMENT_COMPLETE.invoker().onAdvancementComplete(owner, advancement, criterionName);
	}
}
