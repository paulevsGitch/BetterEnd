package ru.betterend.mixin.client;

import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.client.render.ArmoredElytraLayer;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {
	
	public ArmorStandRendererMixin(EntityRendererProvider.Context context, ArmorStandArmorModel entityModel, float f) {
		super(context, entityModel, f);
	}
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	public void be_addCustomLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
		addLayer(new ArmoredElytraLayer<>(this, context.getModelSet()));
	}
}
