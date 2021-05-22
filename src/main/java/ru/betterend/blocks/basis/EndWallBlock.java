package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class EndWallBlock extends WallBlock implements BlockModelProvider {
	
	private final Block parent;
	
	public EndWallBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion());
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_WALL, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("side_tall")) {
			return Patterns.createJson(Patterns.BLOCK_WALL_SIDE_TALL, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("side")) {
			return Patterns.createJson(Patterns.BLOCK_WALL_SIDE, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_WALL_POST, parentId.getPath(), blockId.getPath());
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_WALL, parentId.getPath(), blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation thisId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		ResourceLocation postId = new ResourceLocation(thisId.getNamespace(),
				"block/" + thisId.getPath() + "_post");
		ResourceLocation sideId = new ResourceLocation(thisId.getNamespace(),
				"block/" + thisId.getPath() + "_side");
		ResourceLocation sideTallId = new ResourceLocation(thisId.getNamespace(),
				"block/" + thisId.getPath() + "_side_tall");
		Optional<String> pattern = Optional.empty();
		if (blockId.equals(postId)) {
			pattern = Patterns.createJson(Patterns.BLOCK_WALL_POST, parentId.getPath(), blockId.getPath());
		}
		if (blockId.equals(sideId)) {
			pattern = Patterns.createJson(Patterns.BLOCK_WALL_SIDE, parentId.getPath(), blockId.getPath());
		}
		if (blockId.equals(sideTallId)) {
			pattern = Patterns.createJson(Patterns.BLOCK_WALL_SIDE_TALL, parentId.getPath(), blockId.getPath());
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation blockId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation postId = new ResourceLocation(blockId.getNamespace(),
				"block/" + blockId.getPath() + "_post");
		ResourceLocation sideId = new ResourceLocation(blockId.getNamespace(),
				"block/" + blockId.getPath() + "_side");
		ResourceLocation sideTallId = new ResourceLocation(blockId.getNamespace(),
				"block/" + blockId.getPath() + "_side_tall");
		registerBlockModel(postId, postId, blockState, modelCache);
		registerBlockModel(sideId, sideId, blockState, modelCache);
		registerBlockModel(sideTallId, sideTallId, blockState, modelCache);

		ModelsHelper.MultiPartBuilder builder = ModelsHelper.MultiPartBuilder.create(stateDefinition);
		builder.part(sideId).setCondition(state -> state.getValue(NORTH_WALL) == WallSide.LOW).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(EAST_WALL) == WallSide.LOW)
				.setTransformation(BlockModelRotation.X0_Y90.getRotation()).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(SOUTH_WALL) == WallSide.LOW)
				.setTransformation(BlockModelRotation.X0_Y180.getRotation()).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(WEST_WALL) == WallSide.LOW)
				.setTransformation(BlockModelRotation.X0_Y270.getRotation()).setUVLock(true).add();
		builder.part(sideTallId).setCondition(state -> state.getValue(NORTH_WALL) == WallSide.TALL).setUVLock(true).add();
		builder.part(sideTallId).setCondition(state -> state.getValue(EAST_WALL) == WallSide.TALL)
				.setTransformation(BlockModelRotation.X0_Y90.getRotation()).setUVLock(true).add();
		builder.part(sideTallId).setCondition(state -> state.getValue(SOUTH_WALL) == WallSide.TALL)
				.setTransformation(BlockModelRotation.X0_Y180.getRotation()).setUVLock(true).add();
		builder.part(sideTallId).setCondition(state -> state.getValue(WEST_WALL) == WallSide.TALL)
				.setTransformation(BlockModelRotation.X0_Y270.getRotation()).setUVLock(true).add();
		builder.part(postId).setCondition(state -> state.getValue(UP)).add();

		return builder.build();
	}
}
