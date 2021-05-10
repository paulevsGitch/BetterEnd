package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndAnvilBlock extends AnvilBlock implements BlockPatterned {
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
}
