package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.core.Registry;
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
import ru.betterend.blocks.BlockProperties;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
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
		builder.add(DESTRUCTION);
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
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%anvil%", blockId.getPath());
		map.put("%top%", getTop(blockId, block));
		return Patterns.createJson(Patterns.BLOCK_ANVIL, map);
	}

	protected String getTop(ResourceLocation blockId, String block) {
		if (block.contains("item")) {
			return blockId.getPath() + "_top_0";
		}
		char last = block.charAt(block.length() - 1);
		return blockId.getPath() + "_top_" + last;
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_ANVIL;
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		return getBlockModel(blockId, defaultBlockState());
	}

	@Override
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		IntegerProperty destructionProperty = getDestructionProperty();
		int destruction = blockState.getValue(destructionProperty);
		String name = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%anvil%", name);
		textures.put("%top%", name + "_top_" + destruction);
		String pattern = Patterns.createJson(Patterns.BLOCK_ANVIL, textures);
		return BlockModel.fromString(pattern);
	}

	@Override
	public MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState) {
		IntegerProperty destructionProperty = getDestructionProperty();
		int destruction = blockState.getValue(destructionProperty);
		String modId = resourceLocation.getNamespace();
		String modelId = "block/" + resourceLocation.getPath() + "_top_" + destruction;
		ResourceLocation modelLocation = new ResourceLocation(modId, modelId);
		System.out.println(modelLocation);
		ModelsHelper.addBlockState(blockState, modelLocation);
		return ModelsHelper.createFacingModel(modelLocation, blockState.getValue(FACING).getOpposite());
	}
}
