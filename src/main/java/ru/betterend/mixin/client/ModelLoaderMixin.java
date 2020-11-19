package ru.betterend.mixin.client;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.IdentifiedContext;
import ru.betterend.patterns.Patterned;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	
	@Final
	@Shadow
	private DeserializationContext variantMapDeserializationContext;
	
	@Final
	@Shadow
	private ResourceManager resourceManager;
	
	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
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
					Identifier itemId = new Identifier(id.getNamespace(), data[1]);
					Optional<Block> block = Registry.BLOCK.getOrEmpty(itemId);
					if (block.isPresent()) {
						if (block.get() instanceof Patterned) {
							Patterned patterned = (Patterned) block.get();
							model = this.be_getModel(data, id, patterned);
							info.setReturnValue(model);
							info.cancel();
						}
					} else {
						Optional<Item> item = Registry.ITEM.getOrEmpty(itemId);
						if (item.isPresent() && item.get() instanceof Patterned) {
							Patterned patterned = (Patterned) item.get();
							model = this.be_getModel(data, id, patterned);
							info.setReturnValue(model);
							info.cancel();
						}
					}
				}
			}
		}
	}
	
	private JsonUnbakedModel be_getModel(String data[], Identifier id, Patterned patterned) {
		String pattern;
		if (id.getPath().contains("item")) {
			pattern = patterned.getModelPattern(id.getPath());
		} else {
			if (data.length > 2) {
				pattern = patterned.getModelPattern(data[2]);
			} else {
				pattern = patterned.getModelPattern(data[1]);
			}
		}
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(pattern);
		model.id = id.toString();
		
		return model;
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
