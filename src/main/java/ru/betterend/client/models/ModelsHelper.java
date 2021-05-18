package ru.betterend.client.models;

import com.google.common.collect.*;
import com.mojang.math.Transformation;
import javafx.print.Collation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
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
}
