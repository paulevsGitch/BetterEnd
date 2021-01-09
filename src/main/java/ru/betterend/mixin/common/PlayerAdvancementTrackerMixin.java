package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.item.GuideBookItem;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
	
	@Shadow
	private ServerPlayerEntity owner;
	
	@Inject(method = "grantCriterion", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			shift = Shift.AFTER))
	public void grantBonuses(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> info) {
		Identifier advId = new Identifier("minecraft:end/enter_end_gateway");
		if (BetterEnd.hasGuideBook() && advId.equals(advancement.getId())) {
			this.owner.giveItemStack(new ItemStack(GuideBookItem.GUIDE_BOOK));
		}
	}
}
