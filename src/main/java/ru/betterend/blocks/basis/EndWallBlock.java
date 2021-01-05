package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndWallBlock extends WallBlock implements BlockPatterned {
	
	private final Block parent;
	
	public EndWallBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque());
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_WALL, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("side_tall")) {
			return Patterns.createJson(Patterns.BLOCK_WALL_SIDE_TALL, parentId.getPath(), blockId.getPath());
		}
		if (block.contains("side")) {
			return Patterns.createJson(Patterns.BLOCK_WALL_SIDE, parentId.getPath(), blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_WALL_POST, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_WALL;
	}
}
