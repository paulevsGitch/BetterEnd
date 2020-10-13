package ru.betterend.mixin.client;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.spongepowered.asm.mixin.Final;
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
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import ru.betterend.BetterEnd;
import ru.betterend.interfaces.IdentifiedContext;
import ru.betterend.interfaces.Patterned;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	
	@Final
	@Shadow
	private DeserializationContext variantMapDeserializationContext;
	
	@Final
	@Shadow
	private ResourceManager resourceManager;
	
	@Inject(method = "loadModelFromJson", cancellable = true, at = @At(
			value = "NEW",
			target = "net/minecraft/util/Identifier",
			shift = Shift.BEFORE))
	private void loadModelPattern(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			Identifier modelId = new Identifier(id.getNamespace(), "models/" + id.getPath() + ".json");
			JsonUnbakedModel model;
			try (Resource resource = this.resourceManager.getResource(modelId)) {
				Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
				model = JsonUnbakedModel.deserialize(reader);
				model.id = id.toString();
				info.setReturnValue(model);
				info.cancel();
			} catch (Exception ex) {
				String data[] = id.getPath().split("/");
				if (data.length > 1) {
					Identifier blockId = new Identifier(id.getNamespace(), data[1]);
					Block block = Registry.BLOCK.get(blockId);
					if (block instanceof Patterned) {
						String pattern;
						if (id.getPath().contains("item")) {
							pattern = ((Patterned) block).getModelPattern(id.getPath());
						} else {
							if (data.length > 2) {
								pattern = ((Patterned) block).getModelPattern(data[2]);
							} else {
								pattern = ((Patterned) block).getModelPattern(data[1]);
							}
						}
						model = JsonUnbakedModel.deserialize(pattern);
						model.id = id.toString();
						info.setReturnValue(model);
						info.cancel();
					}
				}
			}
		}
	}
	
	@Inject(method = "loadModel", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/model/json/ModelVariantMap$DeserializationContext;setStateFactory(Lnet/minecraft/state/StateManager;)V",
		shift = Shift.AFTER))
	private void appendContextID(Identifier id, CallbackInfo info) {
		IdentifiedContext context = IdentifiedContext.class.cast(variantMapDeserializationContext);
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			context.setContextId(BetterEnd.makeID("pattern/" + id.getPath()));
		} else {
			context.setContextId(null);
		}
	}
}
