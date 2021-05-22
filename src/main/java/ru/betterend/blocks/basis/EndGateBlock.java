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
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

public class EndGateBlock extends FenceGateBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndGateBlock(Block source) {
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
		if (block.contains("wall")) {
			if (block.contains("open")) {
				return Patterns.createJson(Patterns.BLOCK_GATE_OPEN_WALL, parentId.getPath(), blockId.getPath());
			} else {
				return Patterns.createJson(Patterns.BLOCK_GATE_CLOSED_WALL, parentId.getPath(), blockId.getPath());
			}
		}
		if (block.contains("open")) {
			return Patterns.createJson(Patterns.BLOCK_GATE_OPEN, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_GATE_CLOSED, parentId.getPath(), blockId.getPath());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		boolean inWall = blockState.getValue(IN_WALL);
		boolean isOpen = blockState.getValue(OPEN);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern;
		if (inWall) {
			pattern = isOpen ? Patterns.createJson(Patterns.BLOCK_GATE_OPEN_WALL, parentId.getPath(), blockId.getPath()) :
					Patterns.createJson(Patterns.BLOCK_GATE_CLOSED_WALL, parentId.getPath(), blockId.getPath());
		} else {
			pattern = isOpen ? Patterns.createJson(Patterns.BLOCK_GATE_OPEN, parentId.getPath(), blockId.getPath()) :
					Patterns.createJson(Patterns.BLOCK_GATE_CLOSED, parentId.getPath(), blockId.getPath());
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		boolean inWall = blockState.getValue(IN_WALL);
		boolean isOpen = blockState.getValue(OPEN);
		String state = "" + (inWall ? "_wall" : "") + (isOpen ? "_open" : "_closed");
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath() + state);
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);
		return ModelsHelper.createFacingModel(modelId, blockState.getValue(FACING), true, false);
	}
}