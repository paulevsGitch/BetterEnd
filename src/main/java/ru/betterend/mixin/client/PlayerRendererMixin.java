package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import ru.betterend.client.render.ArmoredElytraLayer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher, PlayerModel<AbstractClientPlayer> entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;Z)V", at = @At("TAIL"))
	public void be_addCustomLayer(EntityRenderDispatcher entityRenderDispatcher, boolean bl, CallbackInfo info) {
		addLayer(new ArmoredElytraLayer<>(PlayerRenderer.class.cast(this)));
	}
}
