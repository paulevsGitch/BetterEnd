package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndPillarBlock extends PillarBlock implements BlockPatterned {
	public EndPillarBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}
	
	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_PILLAR, texture, texture);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_PILLAR;
	}
}
