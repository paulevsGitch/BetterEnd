package ru.betterend.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.item.ArmoredElytra;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void be_checkCustomElytra(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
		ItemStack itemStack = abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST);
		if (itemStack.getItem() instanceof ArmoredElytra) {
			info.cancel();
		}
	}
}
