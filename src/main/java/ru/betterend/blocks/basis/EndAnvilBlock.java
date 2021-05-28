package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.client.models.BlockModelProvider;
import ru.bclib.client.models.ModelsHelper;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.client.models.Patterns;

public class EndAnvilBlock extends AnvilBlock implements BlockModelProvider {
	private static final IntegerProperty DESTRUCTION = BlockProperties.DESTRUCTION;
	protected final int level;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(FabricBlockSettings.copyOf(Blocks.ANVIL).materialColor(color));
		this.level = level;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(getDestructionProperty());
	}

	public IntegerProperty getDestructionProperty() {
		return DESTRUCTION;
	}

	public int getCraftingLevel() {
		return level;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack stack = new ItemStack(this);
		int level = state.getValue(getDestructionProperty());
		stack.getOrCreateTag().putInt("level", level);
		return Collections.singletonList(stack);
	}

	protected String getTop(ResourceLocation blockId, String block) {
		if (block.contains("item")) {
			return blockId.getPath() + "_top_0";
		}
		char last = block.charAt(block.length() - 1);
		return blockId.getPath() + "_top_" + last;
	}

	@Override
	public BlockModel getItemModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		IntegerProperty destructionProperty = getDestructionProperty();
		int destruction = blockState.getValue(destructionProperty);
		String name = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%anvil%", name);
		textures.put("%top%", name + "_top_" + destruction);
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_ANVIL, textures);
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		IntegerProperty destructionProperty = getDestructionProperty();
		int destruction = blockState.getValue(destructionProperty);
		String modId = stateId.getNamespace();
		String modelId = "block/" + stateId.getPath() + "_top_" + destruction;
		ResourceLocation modelLocation = new ResourceLocation(modId, modelId);
		registerBlockModel(stateId, modelLocation, blockState, modelCache);
		return ModelsHelper.createFacingModel(modelLocation, blockState.getValue(FACING), false, false);
	}
}
