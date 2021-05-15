package ru.betterend.patterns;

import java.io.Reader;
import java.util.Collections;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockModelProvider extends ModelProvider {
	String getStatesPattern(Reader data);
	ResourceLocation statePatternId();
	BlockModel getBlockModel(BlockState blockState);
	MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState);

	static BlockModel createBlockModel(ResourceLocation blockId, String pattern) {
		BlockModel model = BlockModel.fromString(pattern);
		ResourceLocation modelLoc = new ResourceLocation(blockId.getNamespace(), "blocks/" + blockId.getPath());
		model.name = modelLoc.toString();
		return model;
	}

	static MultiVariant createFacingModel(ResourceLocation resourceLocation, Direction facing) {
		Transformation transform = new Transformation(null, facing.getRotation(), null, null);
		Variant variant = new Variant(resourceLocation, transform, false, 1);
		return new MultiVariant(Collections.singletonList(variant));
	}
}
