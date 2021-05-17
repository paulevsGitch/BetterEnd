package ru.betterend.client.models;

import com.google.common.collect.*;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModelsHelper {

	private final static BiMap<BlockState, ResourceLocation> STATES_MAP;

	@Nullable
	public static BlockState getBlockState(ResourceLocation resourceLocation) {
		BlockState blockState = STATES_MAP.inverse().get(resourceLocation);
		if (blockState != null) {
			STATES_MAP.remove(blockState);
		}
		return blockState;
	}

	public static void addBlockState(BlockState blockState, ResourceLocation resourceLocation) {
		STATES_MAP.put(blockState, resourceLocation);
	}

	public static BlockModel createBlockItem(ResourceLocation resourceLocation) {
		String pattern = Patterns.createJson(Patterns.ITEM_BLOCK, resourceLocation.getPath());
		return BlockModel.fromString(pattern);
	}

	public static MultiVariant createBlockSimple(ResourceLocation resourceLocation) {
		Variant variant = new Variant(resourceLocation, Transformation.identity(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static MultiVariant createFacingModel(ResourceLocation resourceLocation, Direction facing) {
		BlockModelRotation rotation = BlockModelRotation.by(0, (int) facing.toYRot());
		Variant variant = new Variant(resourceLocation, rotation.getRotation(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	public static MultiVariant createRotatedModel(ResourceLocation resourceLocation, Direction.Axis axis) {
		BlockModelRotation rotation = BlockModelRotation.X0_Y0;
		switch (axis) {
			case X: {
				rotation = BlockModelRotation.by(90, 0);
				break;
			}
			case Z: {
				rotation = BlockModelRotation.by(90, 90);
				break;
			}
		}
		Variant variant = new Variant(resourceLocation, rotation.getRotation(), false, 1);
		return new MultiVariant(Lists.newArrayList(variant));
	}

	static {
		STATES_MAP = HashBiMap.create();
	}
}
