package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.math.Transformation;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.BetterEnd;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

import static net.minecraft.client.resources.model.ModelBakery.MISSING_MODEL_LOCATION;

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
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	@Override
	public @Nullable UnbakedModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation thisId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		ResourceLocation postId = new ResourceLocation(thisId.getNamespace(),
				"block/" + thisId.getPath() + "_post");
		ResourceLocation sideId = new ResourceLocation(thisId.getNamespace(),
				"block/" + thisId.getPath() + "_side");
		if (blockId.equals(postId)) {
			Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FENCE_POST, parentId.getPath(), blockId.getPath());
			return pattern.map(BlockModel::fromString).orElse(null);
		}
		if (blockId.equals(sideId)) {
			Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FENCE_SIDE, parentId.getPath(), blockId.getPath());
			return pattern.map(BlockModel::fromString).orElse(null);
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

		MultiVariant postVariant = new MultiVariant(Lists.newArrayList(
				new Variant(postId, Transformation.identity(), false, 1)));
		MultiVariant sideNorth = new MultiVariant(Lists.newArrayList(
				new Variant(sideId, Transformation.identity(), true, 1)));
		MultiVariant sideEast = new MultiVariant(Lists.newArrayList(
				new Variant(sideId, BlockModelRotation.X0_Y90.getRotation(), true, 1)));
		MultiVariant sideSouth = new MultiVariant(Lists.newArrayList(
				new Variant(sideId, BlockModelRotation.X0_Y180.getRotation(), true, 1)));
		MultiVariant sideWest = new MultiVariant(Lists.newArrayList(
				new Variant(sideId, BlockModelRotation.X0_Y270.getRotation(), true, 1)));
		StateDefinition<Block, BlockState> blockStateDefinition = getStateDefinition();
		return new MultiPart(blockStateDefinition, Lists.newArrayList(
				new Selector(Condition.TRUE, postVariant),
				new Selector(stateDefinition -> state -> state.getValue(NORTH), sideNorth),
				new Selector(stateDefinition -> state -> state.getValue(EAST), sideEast),
				new Selector(stateDefinition -> state -> state.getValue(SOUTH), sideSouth),
				new Selector(stateDefinition -> state -> state.getValue(WEST), sideWest)
		));
	}
}
