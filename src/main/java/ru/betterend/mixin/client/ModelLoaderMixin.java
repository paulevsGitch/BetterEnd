package ru.betterend.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
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
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.world.generator.GeneratorOptions;

import java.io.IOException;
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
						unbakedCache.put(itemLoc, model);
						info.cancel();
					}
				}
			} else {
				ResourceLocation blockstateId = new ResourceLocation(modId, "blockstates/" + path + ".json");
				if (!resourceManager.hasResource(blockstateId)) {
					Block block = Registry.BLOCK.get(clearLoc);
					if (block instanceof BlockModelProvider) {
						block.getStateDefinition().getPossibleStates().forEach(blockState -> {
							System.out.println(blockState);
							ModelResourceLocation stateLoc = BlockModelShaper.stateToModelLocation(clearLoc, blockState);
							MultiVariant modelVariant = ((BlockModelProvider) block).getModelVariant(stateLoc, blockState);
							if (modelVariant != null) {
								cacheAndQueueDependencies(stateLoc, modelVariant);
							} else {
								BetterEnd.LOGGER.warning("Error loading variant: {}", stateLoc);
							}
						});
						info.cancel();
					}
				}
			}
		}
	}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void be_loadModelPattern(ResourceLocation modelId, CallbackInfoReturnable<BlockModel> info) {
		ResourceLocation modelLocation = new ResourceLocation(modelId.getNamespace(), "models/" + modelId.getPath() + ".json");
		if (!resourceManager.hasResource(modelLocation)) {
			BlockState blockState = ModelsHelper.getBlockState(modelId);
			if (blockState != null) {
				Block block = blockState.getBlock();
				if (block instanceof BlockModelProvider) {
					ResourceLocation blockId = Registry.BLOCK.getKey(block);
					BlockModel model = ((BlockModelProvider) block).getBlockModel(blockId, blockState);
					if (model != null) {
						model.name = modelId.toString();
						info.setReturnValue(model);
					} else {
						BetterEnd.LOGGER.warning("Error loading model: {}", modelId);
					}
				}
			} else {
				String[] data = modelId.getPath().split("/");
				if (data.length > 1) {
					ResourceLocation itemId = new ResourceLocation(modelId.getNamespace(), data[1]);
					Optional<Block> block = Registry.BLOCK.getOptional(itemId);
					if (block.isPresent()) {
						if (block.get() instanceof ModelProvider) {
							ModelProvider modelProvider = (ModelProvider) block.get();
							BlockModel model = be_getModel(data, modelId, modelProvider);
							info.setReturnValue(model);
						}
					} else {
						Optional<Item> item = Registry.ITEM.getOptional(itemId);
						if (item.isPresent() && item.get() instanceof ModelProvider) {
							ModelProvider modelProvider = (ModelProvider) item.get();
							BlockModel model = be_getModel(data, modelId, modelProvider);
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
