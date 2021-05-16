package ru.betterend.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.BetterEnd;
import ru.betterend.patterns.BlockModelProvider;
import ru.betterend.patterns.ModelProvider;
import ru.betterend.world.generator.GeneratorOptions;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
	@Final
	@Shadow
	private ResourceManager resourceManager;
	@Final
	@Shadow
	private Map<ResourceLocation, UnbakedModel> unbakedCache;

	@Shadow
	protected abstract void cacheAndQueueDependencies(ResourceLocation resourceLocation, UnbakedModel unbakedModel);

	@Shadow
	protected abstract BlockModel loadBlockModel(ResourceLocation resourceLocation);

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void be_loadModels(ResourceLocation resourceLocation, CallbackInfo info) {
		if (resourceLocation instanceof ModelResourceLocation) {
			String modId = resourceLocation.getNamespace();
			String path = resourceLocation.getPath();
			ResourceLocation clearLoc = new ResourceLocation(modId, path);
			ModelResourceLocation modelLoc = (ModelResourceLocation) resourceLocation;
			if (Objects.equals(modelLoc.getVariant(), "inventory")) {
				ResourceLocation itemLoc = new ResourceLocation(modId, "item/" + path);
				ResourceLocation itemModelLoc = new ResourceLocation(modId, "models/" + itemLoc.getPath() + ".json");
				if (!resourceManager.hasResource(itemModelLoc)) {
					Item item = Registry.ITEM.get(clearLoc);
					if (item instanceof ModelProvider) {
						BlockModel model = ((ModelProvider) item).getModel(clearLoc);
						if (model != null) {
							model.name = itemLoc.toString();
						} else {
							model = loadBlockModel(itemLoc);
						}
						cacheAndQueueDependencies(modelLoc, model);
						unbakedCache.put(modelLoc, model);
						info.cancel();
					}
				}
			} else {
				ResourceLocation blockstateId = new ResourceLocation(modId, "blockstates/" + path + ".json");
				if (!resourceManager.hasResource(blockstateId)) {
					Block block = Registry.BLOCK.get(clearLoc);
					if (block instanceof BlockModelProvider) {
						block.getStateDefinition().getPossibleStates().forEach(blockState -> {
							ModelResourceLocation stateLoc = BlockModelShaper.stateToModelLocation(clearLoc, blockState);
							MultiVariant modelVariant = ((BlockModelProvider) block).getModelVariant(stateLoc, blockState);
							BlockModel blockModel = ((BlockModelProvider) block).getBlockModel(clearLoc, blockState);
							if (modelVariant != null && blockModel != null) {
								cacheAndQueueDependencies(stateLoc, modelVariant);
								unbakedCache.put(stateLoc, blockModel);
							} else {
								BetterEnd.LOGGER.warning("Error loading models for {}", clearLoc);
							}
						});
						info.cancel();
					}
				}
			}
		}
	}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void be_loadModelPattern(ResourceLocation id, CallbackInfoReturnable<BlockModel> info) {
		if (BetterEnd.isModId(id)) {
			ResourceLocation modelId = new ResourceLocation(id.getNamespace(), "models/" + id.getPath() + ".json");
			BlockModel model;
			try (Resource resource = resourceManager.getResource(modelId)) {
				Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
				model = BlockModel.fromStream(reader);
				model.name = id.toString();
				info.setReturnValue(model);
			} catch (Exception ex) {
				String[] data = id.getPath().split("/");
				if (data.length > 1) {
					ResourceLocation itemId = new ResourceLocation(id.getNamespace(), data[1]);
					Optional<Block> block = Registry.BLOCK.getOptional(itemId);
					if (block.isPresent()) {
						if (block.get() instanceof ModelProvider) {
							ModelProvider modelProvider = (ModelProvider) block.get();
							model = be_getModel(data, id, modelProvider);
							info.setReturnValue(model);
						}
					} else {
						Optional<Item> item = Registry.ITEM.getOptional(itemId);
						if (item.isPresent() && item.get() instanceof ModelProvider) {
							ModelProvider modelProvider = (ModelProvider) item.get();
							model = be_getModel(data, id, modelProvider);
							info.setReturnValue(model);
						}
					}
				}
			}
		}
	}

	private BlockModel be_getModel(String[] data, ResourceLocation id, ModelProvider modelProvider) {
		String pattern;
		if (id.getPath().contains("item")) {
			pattern = modelProvider.getModelString(id.getPath());
		} else {
			if (data.length > 2) {
				pattern = modelProvider.getModelString(data[2]);
			} else {
				pattern = modelProvider.getModelString(data[1]);
			}
		}
		BlockModel model = BlockModel.fromString(pattern);
		model.name = id.toString();
		
		return model;
	}
	
	@ModifyVariable(method = "loadModel", ordinal = 2, at = @At(value = "INVOKE"))
	public ResourceLocation be_switchModel(ResourceLocation id) {
		if (GeneratorOptions.changeChorusPlant() && id.getNamespace().equals("minecraft") && id.getPath().startsWith("blockstates/") && id.getPath().contains("chorus") && !id.getPath().contains("custom_")) {
			id = new ResourceLocation(id.getPath().replace("chorus", "custom_chorus"));
		}
		return id;
	}
}
