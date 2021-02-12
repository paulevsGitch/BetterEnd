package ru.betterend.blocks;

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
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.item.material.EndToolMaterial;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;

public class AeterniumAnvil extends AnvilBlock implements BlockPatterned {
	public static final IntProperty DESTRUCTION = BlockProperties.DESTRUCTION_LONG;
	private final int level;
	
	public AeterniumAnvil() {
		super(FabricBlockSettings.copyOf(Blocks.ANVIL).materialColor(EndBlocks.AETERNIUM_BLOCK.getDefaultMaterialColor()));
		this.level = EndToolMaterial.AETERNIUM.getMiningLevel();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(DESTRUCTION);
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
	
	private String getTop(Identifier blockId, String block) {
		if (block.contains("item")) {
			return blockId.getPath() + "_top_0";
		}
		char last = block.charAt(block.length() - 1);
		return blockId.getPath() + "_top_" + last;
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_ANVIL_LONG;
	}
}
