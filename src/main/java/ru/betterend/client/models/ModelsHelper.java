package ru.betterend.client.models;

import com.google.common.collect.*;
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

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModelsHelper {
	public static BlockModel createBlockItem(ResourceLocation resourceLocation) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_BLOCK, resourceLocation.getPath());
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	public static BlockModel createBlockEmpty(ResourceLocation resourceLocation) {
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_EMPTY, resourceLocation.getPath());
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	public static MultiVariant createBlockSimple(ResourceLocation resourceLocation) {
		Variant variant = new Variant(resourceLocation, Transformation.identity(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static MultiVariant createFacingModel(ResourceLocation resourceLocation, Direction facing) {
		BlockModelRotation rotation = BlockModelRotation.by(0, (int) facing.getOpposite().toYRot());
		Variant variant = new Variant(resourceLocation, rotation.getRotation(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static MultiVariant createRotatedModel(ResourceLocation resourceLocation, Direction.Axis axis) {
		BlockModelRotation rotation = BlockModelRotation.X0_Y0;
		switch (axis) {
			case X: {
				rotation = BlockModelRotation.by(90, 90);
				break;
			}
			case Z: {
				rotation = BlockModelRotation.by(90, 0);
				break;
			}
		}
		Variant variant = new Variant(resourceLocation, rotation.getRotation(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static class MultiPartBuilder {

		private final List<ModelPart> modelParts = Lists.newArrayList();

		private StateDefinition<Block, BlockState> stateDefinition;
		private ModelPart activePart;

		private MultiPartBuilder() {}

		public MultiPartBuilder create(StateDefinition<Block, BlockState> stateDefinition) {
			this.stateDefinition = stateDefinition;
			return this;
		}

		public MultiPartBuilder createPart(ResourceLocation modelId) {
			activePart = new ModelPart(modelId);
			return this;
		}

		public MultiPartBuilder setCondition(Condition condition) {
			activePart.condition = condition;
			return this;
		}

		public MultiPartBuilder setTransformation(Transformation transform) {
			activePart.transform = transform;
			return this;
		}

		public MultiPartBuilder setUVLock(boolean value) {
			activePart.uvLock = value;
			return this;
		}

		public MultiPartBuilder savePart() {
			if (activePart != null) {
				modelParts.add(activePart);
			}
			return this;
		}

		public MultiPart build() {
			List<Selector> selectors = Lists.newArrayList();
			modelParts.forEach(modelPart -> {
				MultiVariant variant = new MultiVariant(Lists.newArrayList(
						new Variant(modelPart.modelId, modelPart.transform, modelPart.uvLock, 1)));
				selectors.add(new Selector(modelPart.condition, variant));
			});
			modelParts.clear();
			return new MultiPart(stateDefinition, selectors);
		}

		private static class ModelPart {
			private final ResourceLocation modelId;
			private Transformation transform = Transformation.identity();
			private Condition condition = Condition.TRUE;
			private boolean uvLock = false;

			private ModelPart(ResourceLocation modelId) {
				this.modelId = modelId;
			}
		}
	}
}
