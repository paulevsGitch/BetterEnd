package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.ModelsHelper.MultiPartBuilder;
import ru.betterend.client.models.Patterns;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EndFenceBlock extends FenceBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndFenceBlock(Block source) {
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
			return Patterns.createJson(Patterns.ITEM_FENCE, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("side")) {
			return Patterns.createJson(Patterns.BLOCK_FENCE_SIDE, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_FENCE_POST, parentId.getPath(), blockId.getPath());
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_FENCE, parentId.getPath(), blockId.getPath());
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
		if (blockId.equals(postId)) {
			Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FENCE_POST, parentId.getPath(), blockId.getPath());
			return ModelsHelper.fromPattern(pattern);
		}
		if (blockId.equals(sideId)) {
			Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FENCE_SIDE, parentId.getPath(), blockId.getPath());
			return ModelsHelper.fromPattern(pattern);
		}
		return null;
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation blockId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation postId = new ResourceLocation(blockId.getNamespace(),
				"block/" + blockId.getPath() + "_post");
		ResourceLocation sideId = new ResourceLocation(blockId.getNamespace(),
				"block/" + blockId.getPath() + "_side");
		registerBlockModel(postId, postId, blockState, modelCache);
		registerBlockModel(sideId, sideId, blockState, modelCache);

		MultiPartBuilder builder = MultiPartBuilder.create(stateDefinition);
		builder.part(sideId).setCondition(state -> state.getValue(NORTH)).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(EAST))
				.setTransformation(BlockModelRotation.X0_Y90.getRotation()).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(SOUTH))
				.setTransformation(BlockModelRotation.X0_Y180.getRotation()).setUVLock(true).add();
		builder.part(sideId).setCondition(state -> state.getValue(WEST))
				.setTransformation(BlockModelRotation.X0_Y270.getRotation()).setUVLock(true).add();
		builder.part(postId).add();

		return builder.build();
	}
}
