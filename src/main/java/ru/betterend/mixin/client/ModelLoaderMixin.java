package ru.betterend.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.BetterEnd;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelProvider;
import ru.betterend.world.generator.GeneratorOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
	private void be_loadModels(ResourceLocation resourceLocation, CallbackInfo info) throws IOException {
		if (resourceLocation instanceof ModelResourceLocation) {
			String modId = resourceLocation.getNamespace();
			String path = resourceLocation.getPath();
			ResourceLocation clearLoc = new ResourceLocation(modId, path);
			ModelResourceLocation modelId = (ModelResourceLocation) resourceLocation;
			if ("inventory".equals(modelId.getVariant())) {
				ResourceLocation itemLoc = new ResourceLocation(modId, "item/" + path);
				ResourceLocation itemModelLoc = new ResourceLocation(modId, "models/" + itemLoc.getPath() + ".json");
				if (!resourceManager.hasResource(itemModelLoc)) {
					Item item = Registry.ITEM.get(clearLoc);
					if (item instanceof ModelProvider) {
						BlockModel model = ((ModelProvider) item).getModel(clearLoc);
						if (model != null) {
							model.name = itemLoc.toString();
							cacheAndQueueDependencies(modelId, model);
							unbakedCache.put(itemLoc, model);
						} else {
							BetterEnd.LOGGER.warning("Error loading model: {}", itemLoc);
						}
						info.cancel();
					}
				}
			} else {
				ResourceLocation stateLoc = new ResourceLocation(modId, "blockstates/" + path + ".json");
				if (!resourceManager.hasResource(stateLoc)) {
					Block block = Registry.BLOCK.get(clearLoc);
					if (block instanceof BlockModelProvider) {
						List<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
						Optional<BlockState> possibleState = possibleStates.stream()
								.filter(state -> modelId.equals(BlockModelShaper.stateToModelLocation(clearLoc, state)))
								.findFirst();
						if (possibleState.isPresent()) {
							UnbakedModel modelVariant = ((BlockModelProvider) block).getModelVariant(modelId, possibleState.get(), unbakedCache);
							if (modelVariant != null) {
								if (modelVariant instanceof MultiPart) {
									possibleStates.forEach(state -> {
										ResourceLocation stateId = BlockModelShaper.stateToModelLocation(clearLoc, state);
										cacheAndQueueDependencies(stateId, modelVariant);
									});
								} else {
									cacheAndQueueDependencies(modelId, modelVariant);
								}
							} else {
								BetterEnd.LOGGER.warning("Error loading variant: {}", modelId);
							}
							info.cancel();
						}
					}
				}
			}
		}
	}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void be_loadModelPattern(ResourceLocation modelId, CallbackInfoReturnable<BlockModel> info) throws IOException {
		ResourceLocation modelLocation = new ResourceLocation(modelId.getNamespace(), "models/" + modelId.getPath() + ".json");
		if (!resourceManager.hasResource(modelLocation)) {
			String[] data = modelId.getPath().split("/");
			if (data.length > 1) {
				ResourceLocation itemId = new ResourceLocation(modelId.getNamespace(), data[1]);
				Optional<Block> block = Registry.BLOCK.getOptional(itemId);
				if (block.isPresent()) {
					if (block.get() instanceof ModelProvider) {
						ModelProvider modelProvider = (ModelProvider) block.get();
						Optional<BlockModel> model = be_getModel(data, modelId, modelProvider);
						if (model.isPresent()) {
							info.setReturnValue(model.get());
						} else {
							throw new FileNotFoundException("Error loading model: " + modelId);
						}
					}
				} else {
					Optional<Item> item = Registry.ITEM.getOptional(itemId);
					if (item.isPresent() && item.get() instanceof ModelProvider) {
						ModelProvider modelProvider = (ModelProvider) item.get();
						Optional<BlockModel> model = be_getModel(data, modelId, modelProvider);
						if (model.isPresent()) {
							info.setReturnValue(model.get());
						} else {
							throw new FileNotFoundException("Error loading model: " + modelId);
						}
					}
				}
			}
		}
	}

	private Optional<BlockModel> be_getModel(String[] data, ResourceLocation id, ModelProvider modelProvider) {
		Optional<String> pattern;
		if (id.getPath().contains("item")) {
			pattern = modelProvider.getModelString(id.getPath());
		} else {
			if (data.length > 2) {
				pattern = modelProvider.getModelString(data[2]);
			} else {
				pattern = modelProvider.getModelString(data[1]);
			}
		}
		if (pattern.isPresent()) {
			BlockModel model = BlockModel.fromString(pattern.get());
			model.name = id.toString();
			return Optional.of(model);
		}
		return Optional.empty();
	}
	
	@ModifyVariable(method = "loadModel", ordinal = 2, at = @At(value = "INVOKE"))
	public ResourceLocation be_switchModel(ResourceLocation id) {
		if (GeneratorOptions.changeChorusPlant() && id.getNamespace().equals("minecraft") && id.getPath().startsWith("blockstates/") && id.getPath().contains("chorus") && !id.getPath().contains("custom_")) {
			id = new ResourceLocation(id.getPath().replace("chorus", "custom_chorus"));
		}
		return id;
	}
}
