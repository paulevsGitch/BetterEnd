package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class EndStairsBlock extends StairBlock implements BlockModelProvider {
	
	private final Block parent;
	
	public EndStairsBlock(Block source) {
		super(source.defaultBlockState(), FabricBlockSettings.copyOf(source));
		this.parent = source;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Optional.empty();
		switch (blockState.getValue(SHAPE)) {
			case STRAIGHT:
				pattern = Patterns.createJson(Patterns.BLOCK_STAIR, parentId.getPath(), blockId.getPath());
				break;
			case INNER_LEFT:
			case INNER_RIGHT:
				pattern = Patterns.createJson(Patterns.BLOCK_STAIR_INNER, parentId.getPath(), blockId.getPath());
				break;
			case OUTER_LEFT:
			case OUTER_RIGHT:
				pattern = Patterns.createJson(Patterns.BLOCK_STAIR_OUTER, parentId.getPath(), blockId.getPath());
				break;
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String state = "";
		StairsShape shape = blockState.getValue(SHAPE);
		switch (shape) {
			case INNER_LEFT:
			case INNER_RIGHT:
				state = "_inner"; break;
			case OUTER_LEFT:
			case OUTER_RIGHT:
			default:
				state = "_outer"; break;
		}
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + state);
		registerBlockModel(stateId, modelId, blockState, modelCache);

		boolean isTop = blockState.getValue(HALF) == Half.TOP;
		boolean isLeft = shape == StairsShape.INNER_LEFT ||
						shape == StairsShape.OUTER_LEFT;
		boolean isRight = shape == StairsShape.INNER_RIGHT ||
						shape == StairsShape.OUTER_RIGHT;
		int y = 0;
		int x = isTop ? 180 : 0;
		switch (blockState.getValue(FACING)) {
			case NORTH:
				if (isTop && !isRight) y = 270;
				else if (!isTop) y = isLeft ? 180 : 270;
				break;
			case EAST:
				if (isTop && isRight) y = 90;
				else if (!isTop && isLeft) y = 270;
				break;
			case SOUTH:
				if (isTop) y = isRight ? 180 : 90;
				else if (!isLeft) y = 90;
				break;
			case WEST:
			default:
				y = (isTop && isRight) ? 270 : (!isTop && isLeft) ? 90 : 180;
				break;
		}
		BlockModelRotation rotation = BlockModelRotation.by(x, y);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), true);
	}
}
