package ru.betterend.client.models;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.mojang.math.Transformation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Environment(EnvType.CLIENT)
public class ModelsHelper {
	public static BlockModel fromPattern(Optional<String> pattern) {
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	public static BlockModel createItemModel(String name) {
		Optional<String> pattern = Patterns.createItemGenerated("item/" + name);
		return fromPattern(pattern);
	}

	public static BlockModel createHandheldItem(String name) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_HANDHELD, "item/" + name);
		return fromPattern(pattern);
	}

	public static BlockModel createBlockItem(ResourceLocation resourceLocation) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_BLOCK, resourceLocation.getPath());
		return fromPattern(pattern);
	}

	public static BlockModel createBlockEmpty(ResourceLocation resourceLocation) {
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_EMPTY, resourceLocation.getPath());
		return fromPattern(pattern);
	}

	public static MultiVariant createMultiVariant(ResourceLocation resourceLocation, Transformation transform, boolean uvLock) {
		Variant variant = new Variant(resourceLocation, transform, uvLock, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static MultiVariant createBlockSimple(ResourceLocation resourceLocation) {
		return createMultiVariant(resourceLocation, Transformation.identity(), false);
	}

	public static MultiVariant createFacingModel(ResourceLocation resourceLocation, Direction facing, boolean uvLock, boolean inverted) {
		if (inverted) {
			facing = facing.getOpposite();
		}
		BlockModelRotation rotation = BlockModelRotation.by(0, (int) facing.toYRot());
		return createMultiVariant(resourceLocation, rotation.getRotation(), uvLock);
	}

	public static MultiVariant createRotatedModel(ResourceLocation resourceLocation, Direction.Axis axis) {
		BlockModelRotation rotation = BlockModelRotation.X0_Y0;
		switch (axis) {
			case X: rotation = BlockModelRotation.X90_Y90; break;
			case Z: rotation = BlockModelRotation.X90_Y0; break;
			default: break;
		}
		return createMultiVariant(resourceLocation, rotation.getRotation(), false);
	}

	public static MultiVariant createRandomTopModel(ResourceLocation resourceLocation) {
		return new MultiVariant(Lists.newArrayList(
			new Variant(resourceLocation, Transformation.identity(), false, 1),
			new Variant(resourceLocation, BlockModelRotation.X0_Y90.getRotation(), false, 1),
			new Variant(resourceLocation, BlockModelRotation.X0_Y180.getRotation(), false, 1),
			new Variant(resourceLocation, BlockModelRotation.X0_Y270.getRotation(), false, 1)
		));
	}

	public static class MultiPartBuilder {

		private final static MultiPartBuilder BUILDER = new MultiPartBuilder();

		public static MultiPartBuilder create(StateDefinition<Block, BlockState> stateDefinition) {
			BUILDER.stateDefinition = stateDefinition;
			BUILDER.modelParts.clear();
			return BUILDER;
		}

		private final List<ModelPart> modelParts = Lists.newArrayList();
		private StateDefinition<Block, BlockState> stateDefinition;

		private MultiPartBuilder() {}

		public ModelPart part(ResourceLocation modelId) {
			return new ModelPart(modelId);
		}

		public MultiPart build() {
			if (modelParts.size() > 0) {
				List<Selector> selectors = Lists.newArrayList();
				modelParts.forEach(modelPart -> {
					MultiVariant variant = createMultiVariant(modelPart.modelId, modelPart.transform, modelPart.uvLock);
					selectors.add(new Selector(modelPart.condition, variant));
				});
				modelParts.clear();
				return new MultiPart(stateDefinition, selectors);
			}
			throw new IllegalStateException("At least one model part need to be created.");
		}

		public class ModelPart {
			private final ResourceLocation modelId;
			private Transformation transform = Transformation.identity();
			private Condition condition = Condition.TRUE;
			private boolean uvLock = false;

			private ModelPart(ResourceLocation modelId) {
				this.modelId = modelId;
			}

			public ModelPart setCondition(Function<BlockState, Boolean> condition) {
				this.condition = stateDefinition -> condition::apply;
				return this;
			}

			public ModelPart setTransformation(Transformation transform) {
				this.transform = transform;
				return this;
			}

			public ModelPart setUVLock(boolean value) {
				this.uvLock = value;
				return this;
			}

			public void add() {
				modelParts.add(this);
			}
		}
	}
}
