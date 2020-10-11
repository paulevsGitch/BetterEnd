package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	
	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID) && id.getPath().contains("pattern")) {
			System.out.println(id.getPath());
			info.cancel();
		}
	}
}
