package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockBase extends Block implements Patterned {
	public BlockBase(Settings settings) {
		super(settings);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data, String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(Patterned.BASE_BLOCK_MODEL, blockId, block);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.BLOCK_STATES_PATTERN;
	}
}
