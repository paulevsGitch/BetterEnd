package ru.betterend.mixin.client;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.patterns.Patterned;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Final
	@Shadow
	private ResourceManager resourceManager;

	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void be_loadModelPattern(ResourceLocation id, CallbackInfoReturnable<JsonUnbakedModel> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			ResourceLocation modelId = new ResourceLocation(id.getNamespace(), "models/" + id.getPath() + ".json");
			JsonUnbakedModel model;
			try (Resource resource = this.resourceManager.getResource(modelId)) {
				Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
				model = JsonUnbakedModel.deserialize(reader);
				model.id = id.toString();
				info.setReturnValue(model);
			} catch (Exception ex) {
				String data[] = id.getPath().split("/");
				if (data.length > 1) {
					ResourceLocation itemId = new ResourceLocation(id.getNamespace(), data[1]);
					Optional<Block> block = Registry.BLOCK.getOrEmpty(itemId);
					if (block.isPresent()) {
						if (block.get() instanceof Patterned) {
							Patterned patterned = (Patterned) block.get();
							model = this.be_getModel(data, id, patterned);
							info.setReturnValue(model);
						}
					} else {
						Optional<Item> item = Registry.ITEM.getOrEmpty(itemId);
						if (item.isPresent() && item.get() instanceof Patterned) {
							Patterned patterned = (Patterned) item.get();
							model = this.be_getModel(data, id, patterned);
							info.setReturnValue(model);
						}
					}
				}
			}
		}
	}

	private JsonUnbakedModel be_getModel(String data[], ResourceLocation id, Patterned patterned) {
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

	@ModifyVariable(method = "loadModel", ordinal = 2, at = @At(value = "INVOKE"))
	public ResourceLocation be_SwitchModel(ResourceLocation id) {
		if (GeneratorOptions.changeChorusPlant() && id.getNamespace().equals("minecraft")
				&& id.getPath().startsWith("blockstates/") && id.getPath().contains("chorus")
				&& !id.getPath().contains("custom_")) {
			id = new ResourceLocation(id.getPath().replace("chorus", "custom_chorus"));
		}
		return id;
	}
}
