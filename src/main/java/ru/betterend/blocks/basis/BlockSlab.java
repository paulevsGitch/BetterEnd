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
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.Patterned;

public class BlockSlab extends SlabBlock implements Patterned {
	
	private final static Identifier STATES_PATTERN = BetterEnd.makeID("patterns/blockstate/pattern_slab.json");
	private final static Identifier MODEL_PATTERN = BetterEnd.makeID("patterns/block/pattern_slab.json");
	
	private final Block parent;
	
	public BlockSlab(Block source) {
		super(FabricBlockSettings.copyOf(source));
		this.parent = source;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String blockStatePattern(String name) {
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterned.createJson(STATES_PATTERN, parentId.getPath());
	}
	
	@Override
	public String modelPattern(String name) {
		Identifier parentId = Registry.BLOCK.getId(parent);
		return Patterned.createJson(MODEL_PATTERN, parentId.getPath());
	}
	
	public Identifier statePatternId() {
		return STATES_PATTERN;
	}
}
