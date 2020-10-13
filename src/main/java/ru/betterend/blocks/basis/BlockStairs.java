package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockStairs extends StairsBlock implements Patterned {
	
	private final Block parent;
	
	public BlockStairs(Block source) {
		super(source.getDefaultState(), FabricBlockSettings.copyOf(source));
		this.parent = source;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data, String block) {
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterned.createJson(data, parentId, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier parentId = Registry.BLOCK.getId(parent);
		if (block.contains("inner")) {
			return Patterned.createJson(Patterned.STAIRS_MODEL_INNER, parentId, block);
		}
		if (block.contains("outer")) {
			return Patterned.createJson(Patterned.STAIRS_MODEL_OUTER, parentId, block);
		}
		return Patterned.createJson(Patterned.STAIRS_MODEL, parentId, block);
	}
	
	public Identifier statePatternId() {
		return Patterned.STAIRS_STATES_PATTERN;
	}
}
