package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class EndSlabBlock extends SlabBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndSlabBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
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
		return Patterns.createJson(Patterns.BLOCK_SLAB, parentId.getPath(), blockId.getPath());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_SLAB, parentId.getPath(), blockId.getPath());
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	@Override
	public MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		SlabType type = blockState.getValue(TYPE);
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath() + "_" + type);
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);
		if (type == SlabType.TOP) {
			BlockModelRotation rotation = BlockModelRotation.by(180, 0);
			Variant variant = new Variant(modelId, rotation.getRotation(), true, 1);
			return new MultiVariant(Lists.newArrayList(variant));
		}
		return ModelsHelper.createBlockSimple(modelId);
	}
}
