package ru.betterend.client.models;

import java.io.Reader;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockModelProvider extends ModelProvider {
	String getStatesPattern(Reader data);
	ResourceLocation statePatternId();

	default BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String pattern = Patterns.createBlockSimple(resourceLocation.getPath());
		return BlockModel.fromString(pattern);
	}

	default MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath());
		ModelsHelper.addBlockState(blockState, modelId);
		return ModelsHelper.createBlockSimple(modelId);
	}
}
