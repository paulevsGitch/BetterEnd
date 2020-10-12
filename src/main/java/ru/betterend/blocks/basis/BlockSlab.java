package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;

import ru.betterend.BetterEnd;
import ru.betterend.interfaces.Patterned;

public class BlockSlab extends SlabBlock implements Patterned {
	public BlockSlab(Block source) {
		super(FabricBlockSettings.copyOf(source));
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String blockStatePattern(String name) {
		Identifier patternId = BetterEnd.makeID("patterns/blockstate/pattern_slab.json");
		return Patterned.createJson(patternId, name.replace("_slab", ""));
	}
	
	@Override
	public String modelPattern(String name) {
		Identifier patternId = BetterEnd.makeID("patterns/block/pattern_slab.json");
		return Patterned.createJson(patternId, name.replace("_slab", ""));
	}
}
