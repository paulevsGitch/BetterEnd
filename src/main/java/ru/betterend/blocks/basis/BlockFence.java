package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockFence extends FenceBlock implements Patterned {
	
	private final Block parent;
	
	public BlockFence(Block source) {
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
		return Patterned.createJson(data, parentId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (block.contains("item")) {
			return Patterned.createJson(Patterned.ITEM_FENCE, parentId, blockId.getPath());
		}
		if (block.contains("side")) {
			return Patterned.createJson(Patterned.BLOCK_FENCE_SIDE, parentId, blockId.getPath());
		}
		return Patterned.createJson(Patterned.BLOCK_FENCE_POST, parentId, blockId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.STATE_FENCE;
	}
}
