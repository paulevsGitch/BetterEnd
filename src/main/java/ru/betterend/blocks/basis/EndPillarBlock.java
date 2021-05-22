package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class EndPillarBlock extends RotatedPillarBlock implements BlockModelProvider {
	public EndPillarBlock(Properties settings) {
		super(settings);
	}
	
	public EndPillarBlock(Block block) {
		super(FabricBlockSettings.copyOf(block));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

	@Override
	public Optional<String> getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return createBlockPattern(blockId);
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		Optional<String> pattern = createBlockPattern(blockId);
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath());
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);
		return ModelsHelper.createRotatedModel(modelId, blockState.getValue(AXIS));
	}

	protected Optional<String> createBlockPattern(ResourceLocation blockId) {
		return Patterns.createBlockPillar(blockId.getPath());
	}
}
