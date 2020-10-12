package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelVariantMap.DeserializationContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.IdentifiedContext;
import ru.betterend.interfaces.Patterned;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	
	@Shadow
	private DeserializationContext variantMapDeserializationContext;
	
	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID) && id.getPath().contains("pattern")) {
			String data[] = id.getPath().split("/");
			Identifier blockId = new Identifier(id.getNamespace(), data[1]);
			Block block = Registry.BLOCK.get(blockId);
			if (block instanceof Patterned) {
				String pattern = ((Patterned) block).modelPattern(data[1]);
				info.setReturnValue(JsonUnbakedModel.deserialize(pattern));
				info.cancel();
			}
		}
	}
	
	@Inject(method = "loadModel", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/model/json/ModelVariantMap$DeserializationContext;setStateFactory(Lnet/minecraft/state/StateManager;)V",
		shift = Shift.AFTER))
	private void loadModel(Identifier id, CallbackInfo info) {
		IdentifiedContext context = IdentifiedContext.class.cast(variantMapDeserializationContext);
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			context.setContextId(BetterEnd.makeID("pattern/" + id.getPath()));
		} else {
			context.setContextId(null);
		}
	}
}
