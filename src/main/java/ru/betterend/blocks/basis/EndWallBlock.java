package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndWallBlock extends WallBlock implements BlockPatterned {
	
	private final Block parent;
	
	public EndWallBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion());
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return Patterns.createJson(data, parentId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
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
	public ResourceLocation statePatternId() {
		return Patterns.STATE_WALL;
	}
}
