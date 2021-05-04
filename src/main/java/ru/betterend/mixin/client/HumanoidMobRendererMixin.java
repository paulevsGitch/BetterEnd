package ru.betterend.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.client.render.ArmoredElytraLayer;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {

	public HumanoidMobRendererMixin(EntityRenderDispatcher entityRenderDispatcher, M entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void be_addCustomLayer(EntityRenderDispatcher entityRenderDispatcher, M humanoidModel, float f, float g, float h, float i, CallbackInfo info) {
		addLayer(new ArmoredElytraLayer<>(this));
	}
}
