package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import ru.betterend.client.render.ArmoredElytraLayer;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin  extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {

	public ArmorStandRendererMixin(EntityRenderDispatcher entityRenderDispatcher, ArmorStandArmorModel entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void be_addCustomLayer(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo info) {
		addLayer(new ArmoredElytraLayer<>(this));
	}
}
