package ru.betterend.mixin.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import ru.betterend.client.render.ArmoredElytraLayer;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {

	public HumanoidMobRendererMixin(EntityRendererProvider.Context context, M entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At("TAIL"))
	public void be_addCustomLayer(EntityRendererProvider.Context context, M humanoidModel, float f, float g, float h, float i, CallbackInfo ci) {
		addLayer(new ArmoredElytraLayer<>(this, context.getModelSet()));
	}
}
