package ru.betterend.blocks.basis;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class EndButtonBlock extends ButtonBlock implements BlockModelProvider {

	private final Block parent;

	protected EndButtonBlock(Block parent, Properties properties, boolean sensitive) {
		super(sensitive, properties);
		this.parent = parent;
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
			return Patterns.createJson(Patterns.ITEM_BUTTON, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("pressed")) {
			return Patterns.createJson(Patterns.BLOCK_BUTTON_PRESSED, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_BUTTON, parentId.getPath(), blockId.getPath());
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_BUTTON, parentId.getPath(), blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		Optional<String> pattern = blockState.getValue(POWERED) ?
				Patterns.createJson(Patterns.BLOCK_BUTTON_PRESSED, parentId.getPath(), blockId.getPath()) :
				Patterns.createJson(Patterns.BLOCK_BUTTON, parentId.getPath(), blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation resourceLocation, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String powered = blockState.getValue(POWERED) ? "_powered" : "";
		ResourceLocation modelId = new ResourceLocation(resourceLocation.getNamespace(),
				"block/" + resourceLocation.getPath() + powered);
		registerBlockModel(resourceLocation, modelId, blockState, modelCache);
		AttachFace face = blockState.getValue(FACE);
		boolean isCeiling = face == AttachFace.CEILING;
		int x = 0, y = 0;
		switch (face) {
			case CEILING: x = 180; break;
			case WALL: x = 90; break;
		}
		switch (blockState.getValue(FACING)) {
			case NORTH: if (isCeiling) { y = 180; } break;
			case EAST: y = isCeiling ? 270 : 90; break;
			case SOUTH: if(!isCeiling) { y = 180; } break;
			case WEST: y = isCeiling ? 90 : 270; break;
		}
		BlockModelRotation rotation = BlockModelRotation.by(x, y);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), face == AttachFace.WALL);
	}
}
