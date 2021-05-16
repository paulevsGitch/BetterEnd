package ru.betterend.patterns;

import java.io.Reader;
import java.util.Collections;

import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockModelProvider extends ModelProvider {
	String getStatesPattern(Reader data);
	ResourceLocation statePatternId();
	BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState);
	MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState);

	static BlockModel createItemModel(String name) {
		String pattern = Patterns.createItemGenerated("item/" + name);
		return BlockModel.fromString(pattern);
	}

	static BlockModel createBlockModel(ResourceLocation blockId, String pattern) {
		BlockModel model = BlockModel.fromString(pattern);
		ResourceLocation modelLoc = new ResourceLocation(blockId.getNamespace(), "blocks/" + blockId.getPath());
		model.name = modelLoc.toString();
		return model;
	}

	static MultiVariant createBlockSimple(ResourceLocation resourceLocation) {
		Variant variant = new Variant(resourceLocation, Transformation.identity(), false, 1);
		return new MultiVariant(Collections.singletonList(variant));
	}

	static MultiVariant createFacingModel(ResourceLocation resourceLocation, Direction facing) {
		Transformation transform = new Transformation(null, facing.getRotation(), null, null);
		Variant variant = new Variant(resourceLocation, transform, false, 1);
		return new MultiVariant(Collections.singletonList(variant));
	}

	static MultiVariant createRotatedModel(ResourceLocation resourceLocation, Direction.Axis rotation) {
		Transformation transform = Transformation.identity();
		switch (rotation) {
			case X: {
				transform = new Transformation(null, Vector3f.ZP.rotationDegrees(90), null, null);
				break;
			}
			case Z: {
				transform = new Transformation(null, Vector3f.XP.rotationDegrees(90), null, null);
				break;
			}
		}
		Variant variant = new Variant(resourceLocation, transform, false, 1);
		return new MultiVariant(Collections.singletonList(variant));
	}
}
