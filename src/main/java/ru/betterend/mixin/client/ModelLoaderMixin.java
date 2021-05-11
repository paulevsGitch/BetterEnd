package ru.betterend.mixin.client;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.BetterEnd;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterned;
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
		if (BetterEnd.isModId(resourceLocation) && resourceLocation instanceof ModelResourceLocation) {
			String modId = resourceLocation.getNamespace();
			String path = resourceLocation.getPath();
			ResourceLocation clearLoc = new ResourceLocation(modId, path);
			ModelResourceLocation modelLoc = (ModelResourceLocation) resourceLocation;
			if (Objects.equals(modelLoc.getVariant(), "inventory")) {
				ResourceLocation itemLoc = new ResourceLocation(modId, "item/" + path);
				ResourceLocation itemModelLoc = new ResourceLocation(modId, "models/" + itemLoc.getPath() + ".json");
				if (!resourceManager.hasResource(itemModelLoc)) {
					Item item = Registry.ITEM.get(clearLoc);
					if (item instanceof Patterned) {
						BlockModel model = ((Patterned) item).getItemModel();
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
					if (block instanceof BlockPatterned) {
						block.getStateDefinition().getPossibleStates().forEach(blockState -> {
							UnbakedModel model = ((BlockPatterned) block).getBlockModel(blockState);
							if (model != null) {
								ModelResourceLocation stateLoc = BlockModelShaper.stateToModelLocation(clearLoc, blockState);
								cacheAndQueueDependencies(stateLoc, model);
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
						if (block.get() instanceof Patterned) {
							Patterned patterned = (Patterned) block.get();
							model = be_getModel(data, id, patterned);
							info.setReturnValue(model);
						}
					} else {
						Optional<Item> item = Registry.ITEM.getOptional(itemId);
						if (item.isPresent() && item.get() instanceof Patterned) {
							Patterned patterned = (Patterned) item.get();
							model = be_getModel(data, id, patterned);
							info.setReturnValue(model);
						}
					}
				}
			}
		}
	}

	private BlockModel be_getModel(String[] data, ResourceLocation id, Patterned patterned) {
		String pattern;
		if (id.getPath().contains("item")) {
			pattern = patterned.getModelString(id.getPath());
		} else {
			if (data.length > 2) {
				pattern = patterned.getModelString(data[2]);
			} else {
				pattern = patterned.getModelString(data[1]);
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
