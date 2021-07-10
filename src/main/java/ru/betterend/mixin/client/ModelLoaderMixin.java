package ru.betterend.mixin.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
	
	@ModifyVariable(method = "loadModel", ordinal = 2, at = @At(value = "INVOKE"))
	public ResourceLocation be_switchModel(ResourceLocation id) {
		if (GeneratorOptions.changeChorusPlant() && id.getNamespace().equals("minecraft") && id.getPath().startsWith("blockstates/") && id.getPath().contains("chorus") && !id.getPath().contains("custom_")) {
			id = new ResourceLocation(id.getPath().replace("chorus", "custom_chorus"));
		}
		return id;
	}
}
