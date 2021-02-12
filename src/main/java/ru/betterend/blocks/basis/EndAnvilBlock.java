package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndAnvilBlock extends AnvilBlock implements BlockPatterned {
	public static final IntProperty DESTRUCTION = BlockProperties.DESTRUCTION;
	protected final int level;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(FabricBlockSettings.copyOf(Blocks.ANVIL).materialColor(color));
		this.level = level;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(DESTRUCTION);
	}

	public IntProperty getDestructionProperty() {
		return DESTRUCTION;
	}

	public int getCraftingLevel() {
		return level;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%anvil%", blockId.getPath());
		map.put("%top%", getTop(blockId, block));
		return Patterns.createJson(Patterns.BLOCK_ANVIL, map);
	}

	protected String getTop(Identifier blockId, String block) {
		if (block.contains("item")) {
			return blockId.getPath() + "_top_0";
		}
		char last = block.charAt(block.length() - 1);
		return blockId.getPath() + "_top_" + last;
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_ANVIL;
	}
}
